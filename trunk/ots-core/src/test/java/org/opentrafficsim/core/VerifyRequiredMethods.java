package org.opentrafficsim.core;

import static org.junit.Assert.fail;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.junit.Test;

/**
 * Verify that all classes have a toString method.
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Apr 11, 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class VerifyRequiredMethods
{

    /**
     * Check that all classes have a toString method.
     */
    @Test
    public final void toStringTest()
    {
        Collection<Class<?>> classList = ClassList.classList("org.opentrafficsim", true);
        for (Class<?> c : classList)
        {
            if (c.getName().contains("SimulatorFrame"))
            {
                // System.out.println("opletten");
            }
            if (Exception.class.isAssignableFrom(c))
            {
                continue;
            }
            if (ClassList.isAnonymousInnerClass(c))
            {
                continue;
            }
            Method toStringMethod = null;
            boolean allStatic = c.getConstructors().length == 0;
            boolean isAbstract = false;
            for (String modifierString : Modifier.toString(c.getModifiers()).split(" "))
            {
                if (modifierString.equals("abstract"))
                {
                    isAbstract = true;
                    break;
                }
            }
            for (Method m : c.getDeclaredMethods())
            {
                if (m.getName().equals("toString") && m.getParameterCount() == 0)
                {
                    toStringMethod = m;
                }
                if (allStatic)
                {
                    boolean isStatic = false;
                    for (String modifierString : Modifier.toString(m.getModifiers()).split(" "))
                    {
                        if ("static".equals(modifierString))
                        {
                            isStatic = true;
                            break;
                        }
                    }
                    if (!isStatic)
                    {
                        allStatic = false;
                    }
                }
            }
            if (null == toStringMethod)
            {
                try
                {
                    toStringMethod = c.getMethod("toString");
                }
                catch (NoSuchMethodException | SecurityException exception)
                {
                    exception.printStackTrace();
                    fail("getMethod(\"toString\") should never fail");
                }
                boolean isFinal = false;
                for (String modifierString : Modifier.toString(toStringMethod.getModifiers()).split(" "))
                {
                    if ("final".equals(modifierString))
                    {
                        isFinal = true;
                        break;
                    }
                }
                if (isFinal)
                {
                    System.out.println("Class " + c.getName() + " can not override the toString method because a super "
                            + "class implements a final toString method");
                }
                else if (allStatic)

                {
                    System.out.println("Class " + c.getName() + " does not have override the toString method because all "
                            + "methods are static (modifiers: " + Modifier.toString(c.getModifiers()) + ")");
                }
                else if (isAbstract)
                {
                    System.out.println("Class " + c.getName() + " does not have to override the toString method because "
                            + "it is an abstract class");
                }
                else if (c.isEnum())
                {
                    System.out.println("Class " + c.getName() + " does not override toString (this class is an enum)");
                }
                else
                {
                    System.err.println("Class " + c.getName() + " does not override toString");
                }
            }
        }
    }

    /**
     * Check that all classes implement the Serializable interface.
     */
    @Test
    public final void serializableTest()
    {
        Collection<Class<?>> classList = ClassList.classList("org.opentrafficsim", true);
        for (Class<?> c : classList)
        {
            if (Exception.class.isAssignableFrom(c))
            {
                continue;
            }
            boolean allStatic = c.getConstructors().length == 0;
            for (Method m : c.getDeclaredMethods())
            {
                if (allStatic)
                {
                    boolean isStatic = false;
                    for (String modifierString : Modifier.toString(m.getModifiers()).split(" "))
                    {
                        if ("static".equals(modifierString))
                        {
                            isStatic = true;
                            break;
                        }
                    }
                    if (!isStatic)
                    {
                        allStatic = false;
                    }
                }
            }
            if (!Serializable.class.isAssignableFrom(c) && !allStatic && !ClassList.isAnonymousInnerClass(c))
            {
                System.out.println("Class " + c.getName() + " does not implement Serializable");
            }
            // else
            // {
            // System.out.println("Class " + c.getName() + " implements Serializable");
            // }
        }
    }

    /**
     * Check that all classes that implement equals also implement hashCode.
     */
    @Test
    public final void equalsAndHashCodeTest()
    {
        Collection<Class<?>> classList = ClassList.classList("org.opentrafficsim", true);
        for (Class<?> c : classList)
        {
            if (Exception.class.isAssignableFrom(c))
            {
                continue;
            }
            Method equalsMethod = null;
            Method hashCodeMethod = null;
            for (Method m : c.getDeclaredMethods())
            {
                if (m.getName().equals("equals"))
                {
                    equalsMethod = m;
                }
                else if (m.getName().equals("hashCode"))
                {
                    hashCodeMethod = m;
                }
            }
            if (null == equalsMethod)
            {
                if (null == hashCodeMethod)
                {
                    // System.out.println("Class " + c.getName() + " implements neither equals nor hashCode");
                }
                else
                {
                    // System.out.println("Class " + c.getName() + " implements hashCode, but not equals");
                }
            }
            else if (null == hashCodeMethod)
            {
                fail("Class " + c.getName() + " implements equals but NOT hashCode");
            }
            else
            {
                // System.out.println("Class " + c.getName() + " implements equals and hashCode (good)");
            }
        }
    }

}
