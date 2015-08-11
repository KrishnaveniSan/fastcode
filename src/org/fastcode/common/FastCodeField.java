/**
 *
 */
package org.fastcode.common;

import static org.eclipse.jdt.core.Signature.getSignatureSimpleName;
import static org.fastcode.common.FastCodeConstants.DOT;
import static org.fastcode.common.FastCodeConstants.EMPTY_CHAR;
import static org.fastcode.common.FastCodeConstants.EMPTY_STR;
import static org.fastcode.common.FastCodeConstants.LEFT_BRACKET;
import static org.fastcode.common.FastCodeConstants.SPACE;
import static org.fastcode.util.SourceUtil.getFQNameFromFieldTypeName;
import static org.fastcode.util.SourceUtil.isNativeType;
import static org.fastcode.util.StringUtil.changeToCamelCase;
import static org.fastcode.util.StringUtil.flattenType;
import static org.fastcode.util.StringUtil.parseType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMemberValuePair;
import org.fastcode.util.StringUtil;

/**
 * @author Gautam
 *
 */
public class FastCodeField extends AbstractFastCodeField {

	private String									fullName;
	private String									getter;
	private String									setter;
	private IField									field;
	private FastCodeField							parentField;
	private final Map<String, Map<String, String>>	annotations		= new HashMap<String, Map<String, String>>();
	public static final Map<String, String>			defaultValues	= new HashMap<String, String>();
	private boolean									array;
	private String									fullTypeName;
	private int										arrayDimension;
	private final List<FastCodeField>				childFields		= new ArrayList<FastCodeField>();
	private boolean									typeNative;
	private final FastCodeType						type;
	private String									gettersetter;
	private boolean									builderPattern;
	private boolean									object;
	private boolean									empty;
	private boolean									Null;
	private String									completeValue;

	static {
		defaultValues.put("Boolean", "false");
		defaultValues.put("boolean", "false");
		defaultValues.put("Integer", "0");
		defaultValues.put("int", "0");
		defaultValues.put("float", "0.0f");
		defaultValues.put("Float", "0.0f");
		defaultValues.put("Double", "0.0");
		defaultValues.put("double", "0.0");
		defaultValues.put("String", "\"\"");
		defaultValues.put("byte", "0");
		defaultValues.put("char", "''");
		defaultValues.put("long", "0");
		defaultValues.put("short", "0");
	}

	/**
	 * @param field
	 * @throws Exception
	 */
	public FastCodeField(final IField field) throws Exception {
		this(field, field.getElementName());
	}

	/**
	 *
	 * @param field
	 * @param name
	 * @throws Exception
	 */
	public FastCodeField(final IField field, final String name) throws Exception {
		this(field, name, null, null);
	}

	/**
	 * @param name
	 * @param value
	 * @throws Exception
	 * For Json fields
	 */
	public FastCodeField(final String name, final Object value) throws Exception {
		this.name = name;
		this.completeValue = value instanceof String ? "\"" + value.toString() + "\"" : value.toString();
		this.value = parseValue(value.toString());
		this.fullName = name;
		//super(name, value instanceof String ? "\"" + value.toString() + "\"" : value.toString());
		this.type = findType(value);

	}

	/**
	 * @param value
	 * @return
	 */
	private String parseValue(final String value) {
		if (!StringUtil.isEmpty(value)) {
			if (value.matches("\".*\"") || value.startsWith("{") || value.startsWith("[")) {
				return value.substring(1, value.length() - 1);
			} else {
				return value;
			}
		}
		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	private FastCodeType findType(final Object value) {
		FastCodeType jsonFieldType = null;
		if (value == null) {
			setNull(true);
		}
		if (value instanceof org.json.simple.JSONObject) {
			setObject(true);
			setEmpty(true);
		} else if (value instanceof org.json.simple.JSONArray) {
			this.array = true;
			this.arrayDimension = ((org.json.simple.JSONArray) value).size();
		}

		if (value instanceof String) {
			jsonFieldType = new FastCodeType("java.lang.String");
		} else if (value instanceof Boolean) {
			jsonFieldType = new FastCodeType("java.lang.Boolean");
		} else if (value instanceof Integer) {
			jsonFieldType = new FastCodeType("java.lang.Integer");
		} else if (value instanceof Long) {
			jsonFieldType = new FastCodeType("java.lang.Long");
		} else if (value instanceof Short) {
			jsonFieldType = new FastCodeType("java.lang.Short");
		} else if (value instanceof Float) {
			jsonFieldType = new FastCodeType("java.lang.Float");
		} else if (value instanceof Double) {
			jsonFieldType = new FastCodeType("java.lang.Double");
		} else if (value instanceof Character) {
			jsonFieldType = new FastCodeType("java.lang.Character");
		} else if (value instanceof Byte) {
			jsonFieldType = new FastCodeType("java.lang.Byte");
		} else if (value instanceof Date) {
			jsonFieldType = new FastCodeType("java.util.Date");
		}
		return jsonFieldType;
	}

	/**
	 * @param name
	 * @param value
	 * @param parentField
	 * @throws Exception
	 * For Json Fields with parent field
	 */
	public FastCodeField(final String name, final Object value, FastCodeField parentField) throws Exception {
		this.name = name;
		this.completeValue = value instanceof String ? "\"" + value.toString() + "\"" : value.toString();
		this.value = parseValue(value.toString());
		//super(name, value instanceof String ? "\"" + value.toString() + "\"" : value.toString());
		this.type = findType(value);
		this.parentField = parentField;
		if (parentField != null) {
			if (name.contains("[") && parentField.getName().equals(name.substring(0, name.indexOf(LEFT_BRACKET)))) {
				this.fullName = name;
			} else {
				this.fullName = parentField.getName() + DOT + name;
			}
		}
		while (parentField.getParentField() != null) {
			/*System.out.println(parentField.getName());
			System.out.println(parentField.getParentField().getName());
			System.out.println(this.fullName);*/
			if (parentField.getName().contains("[")
					&& !parentField.getParentField().getName()
							.equals(parentField.getName().substring(0, parentField.getName().indexOf(LEFT_BRACKET)))) {
				this.fullName = parentField.getParentField().getName() + DOT + this.fullName;
			} else if (!parentField.getName().contains("[") && !parentField.getName().equals(parentField.getParentField().getName())) {
				this.fullName = parentField.getParentField().getName() + DOT + this.fullName;
			}
			parentField = parentField.getParentField();

		}

	}

	/**
	 *
	 * @param field
	 * @param name
	 * @param value
	 * @throws Exception
	 */
	public FastCodeField(final IField field, final String name, final String value, final FastCodeField parentField) throws Exception {
		parseTypeForArray(getSignatureSimpleName(field.getTypeSignature()));
		final ICompilationUnit fieldCompUnit = field.getCompilationUnit();
		this.field = field;
		this.name = name; // parseTypeForArray(name);
		this.type = parseType(getSignatureSimpleName(this.field.getTypeSignature()), field.getCompilationUnit());
		if (isArray()) {
			this.fullTypeName = fieldCompUnit != null && fieldCompUnit.exists() ? getFQNameFromFieldTypeName(
					parseTypeForArray(this.type.getName()), fieldCompUnit) : parseTypeForArray(this.type.getName());

		} else {
			this.fullTypeName = fieldCompUnit != null && fieldCompUnit.exists() ? getFQNameFromFieldTypeName(this.type.getName(),
					fieldCompUnit) : this.type.getName();
		}

		this.parentField = parentField;
		this.arrayDimension = 0;
		// this.type = getSignatureSimpleName(field.getTypeSignature());//
		// parseTypeForArray(getSignatureSimpleName(field.getTypeSignature()));
		this.value = defaultValues.containsKey(this.type.getName()) ? defaultValues.get(this.type.getName()) : "null";

		this.typeNative = isNativeType(this.type.getName());
		for (final IAnnotation annot : field.getAnnotations()) {
			final Map<String, String> annotElements = new HashMap<String, String>();
			for (final IMemberValuePair memberValuePair : annot.getMemberValuePairs()) {
				annotElements.put(memberValuePair.getMemberName(), memberValuePair.getValue().toString());
			}
			this.annotations.put(StringUtil.isEmpty(annot.getElementName()) ? "value" : annot.getElementName(), annotElements);
		}

		FastCodeField parenField = this.parentField;
		String fullFldName = this.name;
		while (parenField != null) {
			fullFldName = parenField.name + DOT + fullFldName;
			parenField = parenField.parentField;
		}
		this.fullName = fullFldName;

		final String getPrefix = this.type.getName().equalsIgnoreCase("boolean") ? "is" : "get", setPrefix = "set";
		String getter;
		String setter;
		if (this.name != null && this.name.length() > 1) {
			getter = getPrefix + this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
			parenField = this.parentField;
			if (parenField != null) {
				getter = parenField.getter + "()." + getter;
				parenField = parenField.parentField;
			}
		} else if (this.name != null) {
			getter = getPrefix + this.name.toUpperCase();
		} else {
			getter = EMPTY_STR;
		}
		this.getter = getter;
		if (this.name != null && this.name.length() > 1) {
			setter = setPrefix + this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
			parenField = this.parentField;
			if (parenField != null) {
				setter = parenField.getter + "()." + setter;
				parenField = parenField.parentField;
			}
		} else if (this.name != null) {
			setter = setPrefix + this.name.toUpperCase();
		} else {
			setter = EMPTY_STR;
		}
		this.setter = setter;
		this.type.setFullyQualifiedName(flattenType(this.type, true));
		this.type.setName(flattenType(this.type, false));

	}

	/**
	 *
	 * @return
	 */
	public String makeWord() {
		if (StringUtil.isEmpty(this.name)) {
			return EMPTY_STR;
		}
		final String word = changeToCamelCase(this.name, EMPTY_CHAR);
		final String[] words = word.split(SPACE);
		final StringBuilder wordBuilder = new StringBuilder();
		for (final String w : words) {
			wordBuilder.append(w.length() > 1 ? w.substring(0, 1).toUpperCase() + w.substring(1) : w.substring(0, 1).toUpperCase());
			wordBuilder.append(SPACE);

		}
		return wordBuilder.toString().trim();
	}

	/**
	 *
	 * @param type
	 * @return
	 */
	private String parseTypeForArray(final String type) {
		final StringBuilder builder = new StringBuilder();
		boolean arrOpen = false;
		for (final char c : type.trim().toCharArray()) {
			if (Character.isWhitespace(c)) {
				continue;
			}
			if (Character.isJavaIdentifierPart(c) || c == '<' || c == '>') {
				builder.append(c);
			} else if (!arrOpen && c == '[') {
				arrOpen = true;
				this.array = true;
			} else if (arrOpen && c == ']') {
				arrOpen = false;
				this.arrayDimension++;
			}
		}
		return builder.toString();
	}

	/**
	 *
	 * @return
	 */
	public String getNameAsWord() {
		return makeWord();
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 *
	 * @return
	 */
	public IField getField() {
		return this.field;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String getValue() {
		return this.value;
	}

	/**
	 *
	 * @return
	 */
	public String getGetter() {
		return this.getter;
	}

	/**
	 *
	 * @return
	 */
	public String getSetter() {
		return this.setter;
	}

	/**
	 *
	 * @return
	 */
	public FastCodeField getParentField() {
		return this.parentField;
	}

	/**
	 *
	 * @return
	 */
	public String getFullName() {
		return this.fullName;
	}

	/**
	 *
	 * getter method for annotations
	 *
	 * @return
	 *
	 */
	public Map<String, Map<String, String>> getAnnotations() {
		return this.annotations;
	}

	/**
	 *
	 * getter method for array
	 *
	 * @return
	 *
	 */
	public boolean isArray() {
		return this.array;
	}

	public String getFullTypeName() {
		return this.fullTypeName;
	}

	public int getArrayDimension() {
		return this.arrayDimension;
	}

	/**
	 *
	 * getter method for childFields
	 * @return
	 *
	 */
	public List<FastCodeField> getChildFields() {
		return this.childFields;
	}

	/**
	 *
	 * add method for childFields
	 * @param aFastCodeField
	 *
	 */
	public void addChildField(final FastCodeField aFastCodeField) {
		this.childFields.add(aFastCodeField);
	}

	public boolean isTypeNative() {
		return this.typeNative;
	}

	public FastCodeType getType() {
		return this.type;
	}

	/**
	 *
	 * getter method for gettersetter
	 * @return
	 *
	 */
	public String getGettersetter() {
		return this.gettersetter;
	}

	/**
	 *
	 * setter method for gettersetter
	 * @param gettersetter
	 *
	 */
	public void setGettersetter(final String gettersetter) {
		this.gettersetter = gettersetter;
	}

	/**
	 *
	 * getter method for builderPattern
	 * @return
	 *
	 */
	public boolean isBuilderPattern() {
		return this.builderPattern;
	}

	/**
	 *
	 * setter method for builderPattern
	 * @param builderPattern
	 *
	 */
	public void setBuilderPattern(final boolean builderPattern) {
		this.builderPattern = builderPattern;
	}

	public String getCompleteValue() {
		return this.completeValue;
	}

	public void setCompleteValue(final String completeValue) {
		this.completeValue = completeValue;
	}

	/**
	 * @return the object
	 */
	public boolean isObject() {
		return this.object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(final boolean object) {
		this.object = object;
	}

	/**
	 * @return the empty
	 */
	public boolean isEmpty() {
		return this.empty;
	}

	/**
	 * @param empty the empty to set
	 */
	public void setEmpty(final boolean empty) {
		this.empty = empty;
	}

	/**
	 * @return the null
	 */
	public boolean isNull() {
		return this.Null;
	}

	/**
	 * @param _null the null to set
	 */
	public void setNull(final boolean _null) {
		this.Null = _null;
	}

	/**
	 *
	 * getter method for fullyQualifiedName
	 * for json field
	 * will be same as fullName
	 * method added for json users as they call fullName as fullyQualifiedName
	 * @return
	 *
	 */
	public String getFullyQualifiedName() {
		return this.fullName;
	}

}
