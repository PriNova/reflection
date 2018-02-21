import java.lang.reflect.*;

public final class ClassReflection
{
	private static Class clazz=Object.class;
	private static boolean hasClass = false;
	public final ClassReflection()
	{
		this(clazz);
	}

	public final ClassReflection(final Class mClazz)
	{
		this.clazz = mClazz;
	}

	public final ClassReflection(final String className)
	{
		try
		{
			this.clazz = Class.forName(className);
			hasClass = true;
		}
		catch(final ClassNotFoundException e)
		{
			hasClass = false;
			this.toString();
		}
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("");
		if(hasClass)
		{
			sb.append(showClassRoot(clazz))
				.append(getSuperClass(clazz))
				.append(showInterfaces(clazz))
				.append(showConstructors(clazz))
				.append(showFields(clazz))
				.append(showMethods(clazz))
				.append(showClasses(clazz));
			return sb.toString();
		}
		else
		{
			return "No class defined";
		}
	}

//	public final String getString()
//	{
//		return toString();
//	}
//
	public final static String showClassRoot(final Class clazz)
	{
		return ("Descriptor : " + getModifier(clazz.getModifiers()) + " " + clazz.getName() + "\n");
	}

	public final static String getSuperClass(final Class clazz)
	{
		final StringBuilder sb = new StringBuilder("");
		Class superclass = clazz.getSuperclass();
		final Class subClass;
		final String className;
		sb.append("SuperClass:\n");
		//for (Class subClass : clazz.getSuperclass())
		while(superclass != null) 
		{
			className = superclass.getName();
			sb.append("             " + getModifier(superclass.getModifiers()) + " " + className + "\n");
			subClass = superclass;
			superclass = subClass.getSuperclass(); 
		}
		return sb.toString();
	}

	public final static String showInterfaces(final Class clazz)
	{
		final StringBuilder sb = new StringBuilder("");
		sb.append("Interfaces:\n");
		for(final Class theInterface: clazz.getInterfaces())
		{
			sb.append("             " + getModifier(theInterface.getModifiers()) + " " + theInterface.getName() + "\n");
		}
		return sb.toString();
	}

	public final static String showConstructors(final Class clazz)
	{
		final StringBuilder sb = new StringBuilder("");
		sb.append("Constructor:\n");
		for(final Constructor c : clazz.getDeclaredConstructors()) 
		{
			sb.append("             " + c + "\n");
		}
		return sb.toString();
	}

	public final static String showFields(final Class clazz)
	{
		final StringBuilder sb = new StringBuilder("");
		final String fieldName,
			fieldType, fieldModifier;
		sb.append("Attributes:\n");
		for(final Field publicField : clazz.getDeclaredFields()) 
		{
			fieldModifier = getModifier(publicField.getModifiers());
			fieldName = publicField.getName(); 
			fieldType = publicField.getType().getName();
			sb.append("             " + fieldModifier + " " + fieldType + " " + fieldName + "\n"); 
		}
		return sb.toString();
	}

	public final static String showMethods(final Class o) 
	{
		final StringBuilder sb = new StringBuilder("");
		sb.append("Methods:\n");
		final String returnString,
			methodString,
			parameterString, fieldModifier;
		final Class[] parameterTypes,
			exceptions;
		for(final Method method : o.getDeclaredMethods()) 
		{
			//String getDecClass = method.getDeclaringClass().getName();
			if(method.getDeclaringClass().equals(o))
			//if(getDecClass == o.getName())
			{
				fieldModifier = getModifier(method.getModifiers());
				// Rückgabetyp 
				returnString = method.getReturnType().getName(); 

				// Methodenname 
				methodString = method.getName(); 
				sb.append("             " + fieldModifier + " " + returnString + " " + methodString + "("); 

				// Parameter 
				parameterTypes = method.getParameterTypes(); 
				for(final Class pTypes : parameterTypes)
				{ 
					parameterString = pTypes.getName();
					sb.append(parameterString); 
					sb.append(", "); 
				}
				if(parameterTypes.length != 0)
				{
					sb.delete(sb.length() - 2, sb.length());
				}
				sb.append(")");

				// Exceptions 
				exceptions = method.getExceptionTypes(); 

				if(exceptions.length > 0)
				{ 
					sb.append(" throws ");
					for(final Class exc : exceptions)
					{ 
						sb.append(exc.getName()); 
						sb.append(", "); 
					}
					sb.delete(sb.length() - 2, sb.length());
				} 
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	public final static String showClasses(final Class o)
	{
		final StringBuilder sb = new StringBuilder("");
		sb.append("Inner Class:\n");
		for(final Class sub : o.getDeclaredClasses())
		{
			sb.append("             " + sub.toString() + "\n");
			new ClassReflection(sub);
		}
		return sb.toString();
	}

	public static final String getModifier(final int mod)
	{
		final StringBuffer sb = new StringBuffer(); 
		final int len; 
		if((mod & Modifier.PUBLIC) != 0) sb.append("public "); 
		if((mod & Modifier.PRIVATE) != 0) sb.append("private "); 
		if((mod & Modifier.PROTECTED) != 0) sb.append("protected "); 
		/* Canonical order */ 
		if((mod & Modifier.ABSTRACT) != 0) sb.append("abstract "); 
		if((mod & Modifier.STATIC) != 0) sb.append("static "); 
		if((mod & Modifier.FINAL) != 0) sb.append("final "); 
		if((mod & Modifier.TRANSIENT) != 0) sb.append("transient "); 
		if((mod & Modifier.VOLATILE) != 0) sb.append("volatile "); 
		if((mod & Modifier.NATIVE) != 0) sb.append("native "); 
		if((mod & Modifier.SYNCHRONIZED) != 0) sb.append("synchronized "); 
		if((mod & Modifier.INTERFACE) != 0) sb.append("interface "); 
		if((mod & Modifier.STRICT) != 0) sb.append("strict ");
		if((len = sb.length()) > 0)/* trim trailing space */ 
			return sb.toString().substring(0, len - 1); 
		return "";
	}
}
