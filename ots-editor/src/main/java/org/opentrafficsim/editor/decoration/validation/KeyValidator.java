package org.opentrafficsim.editor.decoration.validation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.opentrafficsim.editor.DocumentReader;
import org.opentrafficsim.editor.XsdTreeNode;
import org.w3c.dom.Node;

/**
 * Validator for xsd:key, xsd:keyref and xsd:unique. Functionality is very similar, with all allowing to define multiple fields.
 * They register with the right nodes in the same way. If no {@code KeyValidator} is given in the constructor, the instance will
 * behave as an xsd:key or xsd:unique. These check a range of values for uniqueness and officially only differ in whether all
 * values need to be present. Here this is ignored and they are treated the same. This class will maintain a list of nodes (fed
 * by an external listener) and validate against field uniqueness over those nodes. If another {@code KeyValidator} is given in
 * the constructor, the instance will behave as an xsd:keyref and validate that the field values are, as a set, within the given
 * {@code KeyValidator}.
 * @author wjschakel
 */
public class KeyValidator implements ValueValidator, EventListener
{

    /** */
    private static final long serialVersionUID = 20230912L;

    /** The node defining the xsd:key, xsd:keyref or xsd:unique. */
    private final Node keyNode;

    /** Path where the key was defined. */
    private final String keyPath;

    /** Key that is referred to by an xsd:keyref. */
    private final KeyValidator refer;

    /** Nodes maintained for an xsd:key. */
    private final Map<XsdTreeNode, Set<XsdTreeNode>> nodes = new LinkedHashMap<>();

    /** Name of the attribute the key points to, {@code null} if it points to a child element. */
    private final List<String> attributeNames = new ArrayList<>();

    /** Name of the child element the key points to, {@code null} if it points to an argument. */
    private final List<String> childNames = new ArrayList<>();

    /** Whether to include the value of the node itself (i.e. field reference "."). */
    private boolean includeSelfValue;

    // Properties so an xsd:key change can be reported to an xsd:keyref

    /** Nodes who's value has to match some field in this validator. */
    private Set<XsdTreeNode> valueValidating = new LinkedHashSet<>();

    /** Nodes who's attribute has to match some field in this validator, grouped per attribute name. */
    private Map<String, Set<XsdTreeNode>> attributeValidating = new LinkedHashMap<>();

    /** Key validators (xsd:keyref) that are using this key validator (xsd:key) to validate. */
    private Set<KeyValidator> listeningKeyrefValidators = new LinkedHashSet<>();

    /** Mapping of keyref node in this key validator to the key node its coupled with in some other key validator. */
    private Map<XsdTreeNode, XsdTreeNode> coupledKeyrefNodes = new LinkedHashMap<>();

    /**
     * Constructor.
     * @param keyNode Node; node defining the xsd:key, xsd:unique or xsd:keyref.
     * @param keyPath String; path where the key was defined.
     * @param refer KeyValidator; key that is referred to by an xsd:keyref, {@code null} for an xsd:key/xsd:unique.
     */
    public KeyValidator(final Node keyNode, final String keyPath, final KeyValidator refer)
    {
        this.keyNode = keyNode;
        this.keyPath = keyPath;
        this.refer = refer;
        List<Node> fields = DocumentReader.getChildren(keyNode, "xsd:field");
        for (Node field : fields)
        {
            String value = DocumentReader.getAttribute(field, "xpath");
            if (value.startsWith("@"))
            {
                this.attributeNames.add(value.substring(1));
                continue;
            }
            else if (value.startsWith("ots:"))
            {
                this.childNames.add(value.substring(4));
                continue;
            }
            else if (value.equals("."))
            {
                this.includeSelfValue = true;
                continue;
            }
            throw new UnsupportedOperationException(
                    "Unable to validate keyref that does not point to an attribute (@) or OTS type (ots:).");
        }
        if (refer != null)
        {
            refer.listeningKeyrefValidators.add(this);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String validate(final XsdTreeNode node)
    {
        if (node.getPath().size() == 1)
        {
            return null; // Node was deleted, but is still visible in the GUI tree for a moment
        }
        List<String> values = gatherFields(node);
        if (this.refer == null)
        {
            // xsd:key; all must be present
            if (this.keyNode.getNodeName().equals("xsd:key") && values.contains(null))
            {
                List<String> missing = new ArrayList<>();
                for (int i = 0; i < values.size(); i++)
                {
                    if (values.get(i) == null)
                    {
                        if (i < this.attributeNames.size())
                        {
                            missing.add(this.attributeNames.get(i));
                        }
                        else if (i < this.attributeNames.size() + this.childNames.size())
                        {
                            missing.add(this.childNames.get(i - this.attributeNames.size()));
                        }
                        else
                        {
                            missing.add("Value");
                        }
                    }
                }
                if (missing.size() == 1)
                {
                    return "Insufficient number of values, missing " + missing.get(0) + ".";
                }
                return "Insufficient number of values, missing " + missing + ".";
            }
            // xsd:key or xsd:unique; all must be present allowing null==null on xsd:unique as was captured above for xsd:key.
            if (Collections.frequency(getValues(node).values(), values) > 1)
            {
                if (this.childNames.size() + this.attributeNames.size() == 1)
                {
                    String name = this.attributeNames.isEmpty() ? this.childNames.get(0) : this.attributeNames.get(0);
                    return "Value " + values.get(0) + " for " + name + " is not unique within " + this.keyPath + ".";
                }
                return "Values " + values + " are not unique within " + this.keyPath + ".";
            }
            return null;
        }
        // xsd:keyref referred value is present ?
        Map<XsdTreeNode, List<String>> valueMap = this.refer.getValues(node);
        if (valueMap.containsValue(values))
        {
            for (Entry<XsdTreeNode, List<String>> entry : valueMap.entrySet())
            {
                if (values.equals(entry.getValue()))
                {
                    this.coupledKeyrefNodes.put(node, entry.getKey());
                }
            }
            return null;
        }
        this.coupledKeyrefNodes.remove(node);
        if (values.size() == 1)
        {
            String value = values.get(0);
            if (value.startsWith("{") && value.endsWith("}"))
            {
                return null; // expression
            }
            String name = this.attributeNames.isEmpty() ? (this.childNames.isEmpty() ? "node" : this.childNames.get(0))
                    : this.attributeNames.get(0);
            String[] types = this.refer.getTypeString();
            String typeString = types.length == 1 ? types[0] : Arrays.asList(types).toString();
            return "Value " + value + " for " + name + " does not refer to a known " + typeString + " within " + this.keyPath
                    + ".";
        }
        values.removeIf((value) -> value.startsWith("{") && value.endsWith("}")); // expressions
        return "Values " + values + " do not refer to a known " + this.refer.getTypeString() + " within " + this.keyPath + ".";
    }

    /**
     * Adds node to this key, if applicable. Nodes are stored per parent instance that defines the context at the level of the
     * path at which the key was defined. This method is called by a listener that the root node has set up, for every created
     * node.
     * @param node XsdTreeNode; node to add.
     */
    public void addNode(final XsdTreeNode node)
    {
        boolean isType = false;
        for (String path : getTypeString())
        {
            isType = node.isType(getPath().equals("Ots") ? path : getPath() + "." + path);
            if (isType)
            {
                break;
            }
        }
        if (isType && this.refer == null)
        {
            XsdTreeNode context = getContext(node);
            this.nodes.computeIfAbsent(context, (key) -> new LinkedHashSet<>()).add(node);
            invalidateAllDependent();
        }
        if (isType)
        {
            if (this.includeSelfValue)
            {
                node.addValueValidator(this);
                registerValidating(node, null);
                if (!this.listeningKeyrefValidators.isEmpty())
                {
                    node.addListener(this, XsdTreeNode.VALUE_CHANGED);
                    node.addListener(this, XsdTreeNode.ACTIVATION_CHANGED);
                }
            }
            for (String attribute : this.attributeNames)
            {
                node.addAttributeValidator(attribute, this);
                registerValidating(node, attribute);
                if (!this.listeningKeyrefValidators.isEmpty())
                {
                    node.addListener(this, XsdTreeNode.ATTRIBUTE_CHANGED);
                    node.addListener(this, XsdTreeNode.ACTIVATION_CHANGED);
                }
            }
        }
        for (String child : this.childNames)
        {
            for (String path : getTypeString())
            {
                String fullPath = path + "." + child;
                if (node.getPathString().endsWith(fullPath))
                {
                    node.addValueValidator(this);
                    registerValidating(node, child);
                    if (!this.listeningKeyrefValidators.isEmpty())
                    {
                        node.addListener(this, XsdTreeNode.VALUE_CHANGED);
                        node.addListener(this, XsdTreeNode.ACTIVATION_CHANGED);
                    }
                }
            }
        }
    }

    /**
     * Registers that this xsd:keyref validator validates the field in the given node using either a value or attribute in the
     * referred xsd:key ({@code this.refer}). This is so the xsd:key can notify the nodes in case a value or attribute was
     * changed, or the node was deleted.
     * @param node XsdTreeNode; node that is validated by this validator.
     * @param field String; field name in node.
     */
    private void registerValidating(final XsdTreeNode node, final String field)
    {
        if (this.refer != null)
        {
            int index = getIndex(field);
            /*
             * Index is the field index in this xsd:keyref. We need to figure out whether the field at this index in the xsd:key
             * is either an attribute, or a value. Then we can be notified on relevant changes in in the xsd:key.
             */
            if (index < this.refer.attributeNames.size())
            {
                this.attributeValidating
                        .computeIfAbsent(this.refer.attributeNames.get(index), (attribute) -> new LinkedHashSet<>()).add(node);
            }
            else
            {
                this.valueValidating.add(node);
            }
        }
    }

    /**
     * Returns a node that represent the proper context.
     * @param node XsdTreeNode; any node somewhere in the context.
     * @return XsdTreeNode; node that represent the proper context.
     */
    private XsdTreeNode getContext(final XsdTreeNode node)
    {
        XsdTreeNode context = null;
        List<XsdTreeNode> path = node.getPath();
        for (int index = path.size() - 1; index >= 0; index--)
        {
            if (path.get(index).getPathString().endsWith(getPath()))
            {
                context = path.get(index);
                break;
            }
        }
        return context;
    }

    /**
     * Remove node. It is removed from all contexts and listening keyrefs. This method is called by a listener that the root
     * node has set up, for every removed node.
     * @param node XsdTreeNode; node to remove.
     */
    public void removeNode(final XsdTreeNode node)
    {
        for (KeyValidator keyref : this.listeningKeyrefValidators)
        {
            keyref.valueValidating.remove(node);
            keyref.attributeValidating.values().forEach((s) -> s.remove(node));
        }
        for (Set<XsdTreeNode> set : this.nodes.values())
        {
            if (set.contains(node))
            {
                invalidateAllDependent();
                set.remove(node);
            }
        }
        if (!this.listeningKeyrefValidators.isEmpty())
        {
            node.removeListener(this, XsdTreeNode.VALUE_CHANGED);
            node.removeListener(this, XsdTreeNode.ATTRIBUTE_CHANGED);
            node.removeListener(this, XsdTreeNode.ACTIVATION_CHANGED);
        }
    }

    /**
     * Returns the present values of the fields for each node within the given context.
     * @param node XsdTreeNode; node that is in the right context.
     * @return Map&lt;XsdTreeNode, List&lt;String&gt;&gt;; list of all values per key node.
     */
    private Map<XsdTreeNode, List<String>> getValues(final XsdTreeNode node)
    {
        XsdTreeNode context = getContext(node);
        Map<XsdTreeNode, List<String>> map = new LinkedHashMap<>();
        for (XsdTreeNode otherNode : this.nodes.computeIfAbsent(context, (key) -> new LinkedHashSet<>()))
        {
            if (otherNode.isActive())
            {
                map.put(otherNode, gatherFields(otherNode));
            }
        }
        return map;
    }

    /**
     * Gathers all the field values, i.e. attribute or child element value. As validators are registered with the node that has
     * the value, attributes are gathered from the given node, while element values are taken from the correctly named children
     * of the parent. Empty values are returned as {@code null}.
     * @param node XsdTreeNode; node for which to get the information.
     * @return List&lt;String&gt;; field values.
     */
    private List<String> gatherFields(final XsdTreeNode node)
    {
        List<String> nodeList = new ArrayList<>();
        for (String attribute : this.attributeNames)
        {
            nodeList.add(node.getAttributeValue(attribute));
        }
        // a child calls this method to validate its value, need to gather all children's values via parent
        List<XsdTreeNode> path = node.getPath();
        XsdTreeNode parent = path.get(path.size() - 2);
        for (String child : this.childNames)
        {
            for (XsdTreeNode treeChild : parent.getChildren())
            {
                if (treeChild.getNodeName().equals(child))
                {
                    nodeList.add(treeChild.getValue());
                }
            }
        }
        if (this.includeSelfValue)
        {
            nodeList.add(node.getValue());
        }
        nodeList.replaceAll((v) -> "".equals(v) ? null : v);
        return nodeList;
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getOptions(final XsdTreeNode node, final String field)
    {
        if (this.refer == null)
        {
            return null;
        }

        /*
         * We gather values from the referred xsd:key, drawing the appropriate context from the node relevant to the xsd:keyref.
         * Can the context of the referred xsd:key be different?
         */
        int index = getIndex(field);
        Map<XsdTreeNode, List<String>> values = this.refer.getValues(node);
        List<String> result = new ArrayList<>(values.size());
        values.forEach((n, list) -> result.add(list.get(index)));
        result.removeIf((v) -> v == null || v.isEmpty());
        return result;
    }

    /**
     * Returns the index of the given field. Indices are based on [attribute field names, children field names, self] in order.
     * @param field String; field name.
     * @return index of the field.
     */
    private int getIndex(final String field)
    {
        /*
         * The following is not robust. The field index might be wrong if 'field', which is the xsd-node name, is equal among
         * attributes, child nodes, and the node itself. E.g. when we are at Route.Node.Node and field = "Node", do we need the
         * value of Route.Node (the node itself), or either a child node or attribute named "Node", which may also both exist?
         */
        // TODO: remember ".", "@", "ots:" to distinguish node value, attribute, or child value
        int index = this.attributeNames.indexOf(field);
        if (index < 0)
        {
            int deltaIndex = this.childNames.indexOf(field);
            if (deltaIndex < 0)
            {
                index = this.attributeNames.size() + this.childNames.size(); // value of node itself appended
            }
            else
            {
                index = this.attributeNames.size() + deltaIndex;
            }
        }
        return index;
    }

    /**
     * Returns the name of the key, i.e. {@code <xsd:keyref name="NAME">}.
     * @return String; name of the key.
     */
    public String getKeyName()
    {
        return DocumentReader.getAttribute(this.keyNode, "name");
    }

    /**
     * Returns the path at which the xsd:key or xsd:keyref is defined.
     * @return String; path at which the xsd:key or xsd:keyref is defined.
     */
    public String getPath()
    {
        return this.keyPath;
    }

    /**
     * Returns the type {@code String} for which the xsd:key or xsd:keyref applies, i.e. "GtuTypes.GtuType" for
     * {@code <xsd:selector xpath=".//ots:GtuTypes/ots:GtuType" />}. Note that multiple paths may be defined separated by "|.
     * @return String[]; type for which the xsd:key or xsd:keyref applies.
     */
    public String[] getTypeString()
    {
        return DocumentReader.getAttribute(DocumentReader.getChild(this.keyNode, "xsd:selector"), "xpath")
                .replace(".//ots:", "").replace("ots:", "").replace("/", ".").split("\\|");
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final Event event) throws RemoteException
    {
        if (XsdTreeNode.ACTIVATION_CHANGED.equals(event.getType()))
        {
            invalidateAllDependent();
        }
        else if (XsdTreeNode.VALUE_CHANGED.equals(event.getType()))
        {
            for (KeyValidator keyref : this.listeningKeyrefValidators)
            {
                for (XsdTreeNode node : keyref.valueValidating)
                {
                    XsdTreeNode keyNode = (XsdTreeNode) ((Object[]) event.getContent())[0];
                    updateReferringKeyrefs(keyNode, this.attributeNames.size() + this.childNames.size(), keyNode.getValue());
                    node.invalidate();
                }
            }
        }
        else if (XsdTreeNode.ATTRIBUTE_CHANGED.equals(event.getType()))
        {
            Object[] content = (Object[]) event.getContent();
            String attribute = (String) content[1];
            for (KeyValidator keyref : this.listeningKeyrefValidators)
            {
                if (keyref.attributeValidating.containsKey(attribute))
                {
                    for (XsdTreeNode node : keyref.attributeValidating.get(attribute))
                    {
                        XsdTreeNode keyNode = (XsdTreeNode) content[0];
                        String newValue = keyNode.getAttributeValue(attribute);
                        int fieldIndex = this.attributeNames.indexOf(attribute);
                        updateReferringKeyrefs(keyNode, fieldIndex, newValue);
                        node.invalidate();
                    }
                }
            }
        }
    }
    
    /**
     * Update value in nodes that refer with xsd:keyref to a value that was changed.
     * @param node XsdTreeNode; node on which the value was changed.
     * @param fieldIndex int; index of field that was changed.
     * @param newValue String; new value.
     */
    private void updateReferringKeyrefs(final XsdTreeNode node, final int fieldIndex, final String newValue)
    {
        for (KeyValidator validator : this.listeningKeyrefValidators)
        {
            for (Entry<XsdTreeNode, XsdTreeNode> entry : validator.coupledKeyrefNodes.entrySet())
            {
                if (entry.getValue().equals(node))
                {
                    if (fieldIndex < validator.attributeNames.size())
                    {
                        entry.getKey().setAttributeValue(validator.attributeNames.get(fieldIndex), newValue);
                    }
                    else
                    {
                        entry.getKey().setValue(newValue);
                    }
                }
            }
        }
    }

    /**
     * Invalidates all nodes that depend on this key, as a key node was added, removed, or made inactive.
     */
    private void invalidateAllDependent()
    {
        for (KeyValidator keyref : this.listeningKeyrefValidators)
        {
            for (XsdTreeNode node : keyref.valueValidating)
            {
                node.invalidate();
            }
            for (Set<XsdTreeNode> set : keyref.attributeValidating.values())
            {
                for (XsdTreeNode node : set)
                {
                    node.invalidate();
                }
            }
        }
    }

}
