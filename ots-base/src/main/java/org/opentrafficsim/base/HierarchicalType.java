package org.opentrafficsim.base;

import java.util.LinkedHashSet;
import java.util.Set;

import org.djutils.exceptions.Throw;

/**
 * Super class for types with hierarchical structure. Upper level types without parent can be created in sub classes using a
 * protected constructor without parent.
 * <p>
 * Copyright (c) 2013-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 * @param <T> Self-reference to type.
 */
public abstract class HierarchicalType<T extends HierarchicalType<T>> extends Type<T> implements Identifiable
{
    /** */
    private static final long serialVersionUID = 20220928L;

    /** The id of the type to make it identifiable. */
    private final String id;

    /** Parent type. */
    private final T parent;

    /** Cached hash code. */
    private int hashCode;

    /** the children of the hierarchical type. */
    private final Set<T> children = new LinkedHashSet<>();

    /**
     * Constructor for creating the top level types in subclasses.
     * @param id String; The id of the type to make it identifiable.
     * @throws NullPointerException if the id is null
     */
    protected HierarchicalType(final String id) throws NullPointerException
    {
        this(id, null);
    }

    /**
     * Constructor that creates a hierarchical type including a link to a parent type.
     * @param id String; The id of the type to make it identifiable.
     * @param parent T; parent type; can be null, in that case no parent will be identified
     * @throws NullPointerException if the id is null
     */
    @SuppressWarnings("unchecked")
    public HierarchicalType(final String id, final T parent) throws NullPointerException
    {
        Throw.whenNull(id, "id cannot be null for hierarchical types");
        this.id = id;
        this.parent = parent;
        if (this.parent != null)
        {
            parent.getChildren().add((T) this);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final String getId()
    {
        return this.id;
    }

    /**
     * Return the parent of this type, or null when no parent was identified.
     * @return parent or {@code null} if this is a top level type.
     */
    public final T getParent()
    {
        return this.parent;
    }

    /**
     * Return the children of this hierarchical type.
     * @return children Set&lt;T&gt; The set of children of the hierarchical type
     */
    public Set<T> getChildren()
    {
        return this.children;
    }

    /**
     * Whether this, or any of the parent types, equals the given type.
     * @param type T; type the type to look for
     * @return whether this, or any of the parent types, equals the given type
     */
    public final boolean isOfType(final T type)
    {
        if (this.equals(type))
        {
            return true;
        }
        if (this.parent != null)
        {
            return this.parent.isOfType(type);
        }
        return false;
    }

    /**
     * Returns the common ancestor of both types.
     * @param type T; other type.
     * @return common ancestor of both types, {@code null} if none
     */
    public final T commonAncestor(final T type)
    {
        T otherType = type;
        while (otherType != null && !isOfType(otherType))
        {
            otherType = otherType.getParent();
        }
        return otherType;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        if (this.hashCode == 0)
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + this.id.hashCode();
            this.hashCode = prime * result + ((this.parent == null) ? 0 : this.parent.hashCode());
        }
        return this.hashCode;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        HierarchicalType<?> other = (HierarchicalType<?>) obj;
        if (!this.id.equals(other.id))
        {
            return false;
        }
        if (this.parent == null)
        {
            if (other.parent != null)
            {
                return false;
            }
        }
        else if (!this.parent.equals(other.parent))
        {
            return false;
        }
        return true;
    }

}
