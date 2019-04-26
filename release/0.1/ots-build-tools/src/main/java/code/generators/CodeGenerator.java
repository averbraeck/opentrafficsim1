package code.generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version 6 okt. 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class CodeGenerator
{
    /** Name of this program. */
    final String generatorName;

    /** Directory where the tree of files will be built. */
    final String buildDir;

    /** The serialVersionUID to put in the generated classes. */
    final String serialVersionUID;

    /** Base directory / package. */
    final String packageBaseName;

    /** Description of the day on which the files were generated. */
    final String when;

    /** Name of the package-info file(s). */
    final static String packageInfoName = "package-info";

    /**
     * Create a new CodeGenerator.
     * @param generatorName String; name of the program that uses this CodeGenerator
     * @param buildDir String; path to directory for generated files and sub-directories
     * @param packageBaseName String; prepended to all package names
     * @param when String; textual description of the date that this CodeGenerator is used
     * @param serialVersionUID String; serialVersionID to put in the generated class files
     */
    public CodeGenerator(final String generatorName, final String buildDir, final String packageBaseName,
            final String when, final Long serialVersionUID)
    {
        this.generatorName = generatorName;
        this.buildDir = buildDir;
        this.packageBaseName = packageBaseName;
        this.when = when;
        this.serialVersionUID = serialVersionUID.toString();
        File testPath = new File(buildDir);
        if (!testPath.exists())
        {
            throw new Error("buildDir (" + buildDir + ") does not exist");
        }
        else if (!testPath.isDirectory())
        {
            throw new Error("buildDir (" + buildDir + ") is not a directory");
        }
    }

    /**
     * Open a file and write the package line and initial block comment.
     * @param relativePackage String; the last element(s) of the package name
     * @param name String; the name of the class file to write
     * @param imports String[]; the imports for the new class file
     * @param description String; the description that is inserted in the block comment at the start of the file
     * @param genericParams String[]; descriptions of the generic parameters of the class
     * @return BufferedWriter; the open file
     */
    public final BufferedWriter openFile(final String relativePackage, final String name, final String[] imports,
            final String description, final String[] genericParams)
    {
        BufferedWriter writer = null;
        String dirList = relativePackage;
        String[] intermediateDirs = dirList.split("[\\.]");
        String path = this.buildDir;
        for (String intermediateDir : intermediateDirs)
        {
            path = path + File.separatorChar + intermediateDir;
        }
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs())
        {
            throw new Error("Cannot create path \"" + path + "\"");
        }
        String fileName = path + File.separatorChar + name + ".java";
        final String packageLine = "package " + this.packageBaseName + "." + relativePackage + ";\r\n";
        try
        {
            writer = new BufferedWriter(new FileWriter(new File(fileName)));
            if (!name.equals(packageInfoName))
            {
                writer.write(packageLine + "\r\n");
            }
            if (null != imports)
            {
                for (String importString : imports)
                {
                    if (null == importString)
                    {
                        continue;
                    }
                    if (importString.length() > 0)
                    {
                        writer.write("import " + importString + ";");
                    }
                    writer.write("\r\n");
                }
                writer.write("\r\n");
            }
            writer.write("/**\r\n * " + description + "\r\n * <p>\r\n * This file was generated by "
                    + this.generatorName + ", " + this.when + "\r\n * <p>\r\n"
                    + " * Copyright (c) 2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the "
                    + "Netherlands. All rights" + " reserved. <br>\r\n"
                    + " * BSD-style license. See <a href=\"http://opentrafficsim.org/node/13\">OpenTrafficSim "
                    + "License</a>.\r\n" + " * <p>\r\n" + " * @version " + this.when + " <br>\r\n"
                    + " * @author <a href=\"http://www.tbm.tudelft.nl/averbraeck\">Alexander Verbraeck</a>\r\n"
                    + " * @author <a href=\"http://www.tudelft.nl/pknoppers\">Peter Knoppers</a>\r\n");
            if (null != genericParams)
            {
                for (String param : genericParams)
                {
                    writer.write(" * @param " + param + "\r\n");
                }
            }
            writer.write(" */\r\n");
            if (name.equals(packageInfoName))
            {
                writer.write(packageLine);
            }
        }
        catch (Exception e)
        {
            throw new Error("Cannot write file " + fileName);
        }
        return writer;
    }

    /**
     * Close a file that was opened with openFile.
     * @param writer BufferedWriter; the result of the preceding call to openFile
     */
    public static void closeFile(final BufferedWriter writer)
    {
        try
        {
            writer.close();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Return the String that defines the serialVersionUID.
     * @param indent String; prepended to each output line
     * @return String
     */
    public final String buildSerialVersionUID(final String indent)
    {
        return buildField(indent, "private static final long serialVersionUID = " + this.serialVersionUID + "L", "");
    }

    /**
     * Write a class file.
     * @param relativePackage String; the last element(s) of the package name
     * @param name String; the name of the class file to write
     * @param imports String[]; the imports for the new class file
     * @param description String; the description that is inserted in the block comment at the start of the file
     * @param genericParams String[]; the descriptions of the generic parameters of the class
     * @param qualifiers String; qualifiers that go before the class key word; e.g. <cite>public abstract</cite>
     * @param typeInfo String; the text that goes immediately after the class name
     * @param generateSerialVersionUID boolean; if true a serialVersionUID is put in the result
     * @param contents String; the text that goes in the class
     */
    public final void generateClass(final String relativePackage, final String name, final String[] imports,
            final String description, final String[] genericParams, final String qualifiers, final String typeInfo,
            final boolean generateSerialVersionUID, final String contents)
    {
        try
        {
            BufferedWriter writer = openFile(relativePackage, name, imports, description, genericParams);
            writer.write(qualifiers + " class " + name
                    + (typeInfo.length() == 0 || typeInfo.startsWith("<") ? "" : " ") + typeInfo + "\r\n{\r\n");
            if (generateSerialVersionUID)
            {
                writer.write(buildSerialVersionUID(indent(1)));
            }
            writer.write(contents + "}\r\n");
            closeFile(writer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Write a class file for an abstract class.
     * @param relativePackage String; the last element(s) of the package name
     * @param name String; the name of the class file to write
     * @param imports String[]; the imports for the new class file
     * @param description String; the description that is inserted in the block comment at the start of the file
     * @param genericParams String[]; the descriptions of the generic parameters of the class
     * @param typeInfo String; the text that goes immediately after the class name
     * @param contents String; the text that goes in the class
     */
    public final void generateAbstractClass(final String relativePackage, final String name, final String[] imports,
            final String description, final String[] genericParams, final String typeInfo, final String contents)
    {
        generateClass(relativePackage, name, imports, description, genericParams, "public abstract", typeInfo, true,
                contents);
    }

    /**
     * Write a class file for a final class that cannot be instantiated.
     * @param relativePackage String; the last element(s) of the package name
     * @param name String; the name of the class file to write
     * @param imports String[]; the imports for the new class file
     * @param description String; the description that is inserted in the block comment at the start of the file
     * @param genericParams String[]; the descriptions of the generic parameters of the class
     * @param typeInfo String; the text that goes immediately after the class name
     * @param contents String; the text that goes in the class
     */
    public final void generateFinalClass(final String relativePackage, final String name, final String[] imports,
            final String description, final String[] genericParams, final String typeInfo, final String contents)
    {
        generateClass(
                relativePackage,
                name,
                imports,
                description,
                genericParams,
                "public final",
                typeInfo,
                false,
                buildMethod(indent(1), "private||" + name, "This class shall never be instantiated.", null, null, null,
                        new String[]{"// Prevent instantiation of this class"}, true) + contents);
    }

    /**
     * Create a String that describes one field.
     * @param indent String; prepended to each output line
     * @param field String; the type and name of the field
     * @param description String; the description of the field
     * @return String
     */
    public final String buildField(final String indent, final String field, final String description)
    {
        return indent + "/** " + description + " */\r\n" + indent + field + ";\r\n\r\n";
    }

    /**
     * Generate a block comment.
     * @param indent String; prefix of all output lines
     * @param comment String; the text to center in the block comment
     * @return String; java code
     */
    public final String buildBlockComment(final String indent, String comment)
    {
        comment = " " + comment + " ";
        StringBuffer construction = new StringBuffer();
        final String pattern = "/**********************************************************************************/";
        construction.append(indent + pattern + "\r\n");
        int halfTruncate = comment.length() / 2;
        construction.append(indent + pattern.substring(0, pattern.length() / 2 - halfTruncate));
        construction.append(comment);
        construction.append(pattern.substring(pattern.length() / 2 + comment.length() - halfTruncate));
        construction.append("\r\n");
        construction.append(indent + pattern + "\r\n\r\n");
        return construction.toString();
    }

    /**
     * Create an indent of N units.
     * @param steps int; the number N
     * @return String
     */
    public final String indent(final int steps)
    {
        final String indent = "    ";

        String result = "";
        for (int i = 0; i < steps; i++)
        {
            result += indent;
        }
        return result;
    }

    /**
     * Create a String that defines one method.
     * @param indent String; prefix for all output lines
     * @param qualifiersTypeAndName String; the qualifiers, the type and the name of the method separated by vertical
     *            bars, e.g. <cite>final public|double|getDoubleValue</cite>. If the method that must be generated
     *            overrides a method in a parent class set this parameter to null.
     * @param description String; description of the method
     * @param parameters String[]; one String for each parameter of the method. Each parameter string consists of
     *            qualifiers, type, name separated by vertical bars, e.g. <cite>final int|index|index of the
     *            entry</cite>. Null entries in the parameters array are silently ignored.
     * @param exceptions String; exception type and description separated by a vertical bar, or null if this method does
     *            not throw exceptions
     * @param pragma String; text that goes after the JavaDoc, but before the start of the method code
     * @param body String[]; the lines of the body of the method. Lines on the outermost level should start with 0
     *            spaces. Null entries in the body array are silently ignored
     * @param constructor boolean; if true; the new method is a constructor; if false; the new method is not a
     *            constructor
     * @return String; the Java source code of the method.
     */
    public final String buildMethod(final String indent, final String qualifiersTypeAndName, final String description,
            final String[] parameters, final String exceptions, final String pragma, final String[] body,
            final boolean constructor)
    {
        final int maxLineLength = 121;
        StringBuilder construction = new StringBuilder();
        String[] fields = qualifiersTypeAndName.split("[|]");
        if (null != description)
        {
            construction.append(indent + "/**\r\n" + indent + " * ");
            construction.append(description);
            construction.append("\r\n");
            if (null != parameters)
            {
                for (String param : parameters)
                {
                    if (null == param)
                    {
                        continue;
                    }
                    String[] paramFields = param.split("[|]");
                    if (3 != paramFields.length)
                    {
                        throw new Error("param should consist of three fields separated by |; got \"" + param + "\"");
                    }
                    if (paramFields[0].startsWith("final "))
                    {
                        paramFields[0] = paramFields[0].substring(6);
                    }
                    String line = indent + " * @param " + paramFields[1] + " " + escapeHTML(paramFields[0]) + ";";
                    String remainder = paramFields[2];
                    String[] words = remainder.split("[ ]");
                    for (String word : words)
                    {
                        if (line.length() + 1 + word.length() >= maxLineLength)
                        {
                            construction.append(line + "\r\n");
                            line = indent + " *" + indent(3) + word;
                        }
                        else
                        {
                            line += " " + word;
                        }
                    }
                    construction.append(line);
                    construction.append("\r\n");
                }
            }
            if (fields.length < 3)
            {
                throw new Error("qualifiersTypeAndName should consist of at least three fields separated by |; got "
                        + qualifiersTypeAndName);
            }
            if (!"void".equals(fields[1]) && !constructor)
            {
                construction.append(indent + " * @return ");
                construction.append(escapeHTML(fields[1]));
                if (4 == fields.length)
                {
                    construction.append("; " + escapeHTML(fields[3]));
                }
                construction.append("\r\n");
            }
            if (null != exceptions)
            {
                String[] exceptionFields = exceptions.split("[|]");
                if (exceptionFields.length != 2)
                {
                    throw new Error("exceptions should consist of two fields separated by |; got \"" + exceptions
                            + "\"");
                }
                construction.append(indent + " * @throws " + exceptionFields[0] + " " + exceptionFields[1] + "\r\n");
            }
            construction.append(indent + " */\r\n");
        }
        else
        {
            construction.append(indent + "/** {@inheritDoc} */\r\n" + indent + "@Override\r\n");
        }
        if (null != pragma)
        {
            if (pragma.length() == 0)
            {
                throw new Error("pragma should not be the empty string");
            }
            construction.append(indent + pragma + "\r\n");
        }
        String line = indent;
        if (fields[0].length() > 0)
        {
            line = line + fields[0] + " ";
        }
        if (fields[1].length() > 0)
        {
            line += fields[1] + " ";
        }
        line += fields[2] + "(";
        String sep = "";
        if (null != parameters)
        {
            for (String param : parameters)
            {
                if (null == param)
                {
                    continue;
                }
                String[] paramFields = param.split("[|]");
                if (!paramFields[1].startsWith("<"))
                {
                    String paramText = paramFields[0] + " " + paramFields[1];
                    if (line.length() + sep.length() + paramText.length() + 1 > maxLineLength)
                    {
                        if (!sep.equals(""))
                        {
                            sep = ",";
                        }
                        construction.append(line + sep + "\r\n");
                        line = indent + indent(2) + paramText;
                    }
                    else
                    {
                        line += sep + paramText;
                    }
                    sep = ", ";
                }
            }
        }
        line += ")";
        construction.append(line);
        if (null != exceptions)
        {
            String append = "throws " + exceptions.split("[|]")[0];
            if (line.length() + append.length() > maxLineLength)
            {
                construction.append("\r\n" + indent + indent(2) + append);
            }
            else
            {
                construction.append(" " + append);
            }
        }
        if (null != body)
        {
            construction.append("\r\n" + indent + "{\r\n");
            final String bodyIndent = indent + indent(1);
            for (String bodyLine : body)
            {
                if (null == bodyLine)
                {
                    continue;
                }
                construction.append(bodyIndent);
                construction.append(bodyLine);
                construction.append("\r\n");
            }
            construction.append(indent + "}\r\n");
        }
        else
        {
            construction.append(";\r\n");
        }
        construction.append("\r\n");
        return construction.toString();
    }

    /**
     * Replace HTML-special character by their escaped versions.
     * @param input String; text to convert to clean HTML
     * @return String; text with correct HTML escapes
     */
    private String escapeHTML(final String input)
    {
        StringBuilder construction = new StringBuilder();
        for (int pos = 0; pos < input.length(); pos++)
        {
            String letter = input.substring(pos, pos + 1);
            if (letter.equals("&"))
            {
                construction.append("&amp;");
            }
            if (letter.equals("<"))
            {
                construction.append("&lt;");
            }
            else if (letter.equals(">"))
            {
                construction.append("&gt;");
            }
            else if (letter.equals("&"))
            {
                construction.append("&amp;");
            }
            else
            {
                construction.append(letter);
            }
        }
        return construction.toString();
    }

    /**
     * Write an interface file that defines an interface that does nothing but define it's own name.
     * @param relativePackage String; the last element(s) of the package name
     * @param name String; the name of the class file to write
     * @param imports String[]; the imports for the new class file
     * @param description String; the description that is inserted in the block comment at the start of the file
     * @param genericParams String[]; the descriptions of the generic parameters of the class
     * @param typeInfo String; the text that goes immediately after the class name
     * @param body String; the body of the interface file
     */
    public final void generateInterface(final String relativePackage, final String name, final String[] imports,
            final String description, final String[] genericParams, final String typeInfo, final String body)
    {
        try
        {
            BufferedWriter writer = openFile(relativePackage, name, imports, description, genericParams);
            writer.write("public interface " + name + (typeInfo.length() > 0 && typeInfo.startsWith("<") ? "" : " ")
                    + typeInfo + "\r\n{\r\n");
            if (null != body)
            {
                writer.write(body);
            }
            else
            {
                writer.write("    // This interface does not force anything to be implemented in classes that "
                        + "implement it\r\n");
            }
            writer.write("}\r\n");
            closeFile(writer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Generate a package-info.java file.
     * @param relativePackageName String; relative package name
     * @param contents String; contents of the package-info file
     */
    public final void generatePackageInfo(final String relativePackageName, final String contents)
    {
        closeFile(openFile(relativePackageName, packageInfoName, null, contents, null));
    }

    /**
     * Build a string with the specified number of <cite>[]</cite> (square bracket) pairs.
     * @param dimensions int; the number of bracket pairs to concatenate
     * @return String
     */
    public String buildEmptyBrackets(final int dimensions)
    {
        return buildBrackets(dimensions, "");
    }

    /**
     * Build a string with the specified number of <cite>[<b>string</b>]</cite> (square bracket with content) pairs.
     * @param dimensions int; the number of bracket pairs with contents to concatenate
     * @param contents String; the text that goes between each pair of brackets
     * @return String
     */
    public String buildBrackets(final int dimensions, final String contents)
    {
        String result = "";
        for (int i = 0; i < dimensions; i++)
        {
            result += "[" + contents + "]";
        }
        return result;
    }

    /**
     * Convert an ArrayList&ltString&gt; into an array of String.
     * @param code ArrayList<String>; the lines to convert to an array of string
     * @return String[]; array containing the strings from the ArrayList
     */
    public static String[] arrayListToArray(final ArrayList<String> code)
    {
        String[] codeLines = new String[code.size()];
        for (int line = 0; line < code.size(); line++)
        {
            codeLines[line] = code.get(line);
        }
        return codeLines;
    }

}