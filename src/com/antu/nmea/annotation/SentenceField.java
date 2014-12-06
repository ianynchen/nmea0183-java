package com.antu.nmea.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Demarkates a field in the parsed nmea object
 * @author yining
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SentenceField {
	
	/**
	 * type of the field, this is used by the codec to decode and encode
	 * the fields. if the fullCodecPath is specified, this field is not used.
	 * this field can be used with the packageName field to specify a codec
	 * full class name. by default, the class name is XXXSentenceFieldCodec,
	 * where XXX is replaced by the value of fieldType with the first letter
	 * capitalized. default package is com.antu.nmea.sentence.field.codec
	 * 
	 * @return
	 */
	String fieldType() default "";
	
	/**
	 * can be used together with the fieldType to specify a custom codec. if
	 * left empty, the default package name for codec is com.antu.nmea.sentence.field.codec.
	 * This field is ignored if fullCodecPath is specified.
	 * @return
	 */
	String packageName() default "";
	
	/**
	 * Allows user to specify a full class name for a specific codec.
	 * @return
	 */
	String fullCodecPath() default "";
	
	/**
	 * Specifies whether a field is required, if a parsing failure or empty field
	 * is given, this will result in the abandoning of the encoding/decoding process.
	 * @return
	 */
	boolean isRequired() default true;

	/**
	 * a numeric order for the alignment of the fields, does not have to be continunous,
	 * but has to be in order per nmea spec.
	 * @return
	 */
	int order();
	
	/**
	 * specifies whether the item comes in sets. used together with groupItemType
	 * @return
	 */
	boolean isGroup() default false;
	
	/**
	 * specifies the type of set, currently only supports "list", which will result in
	 * the generation of a PrintableList
	 * @return
	 */
	String groupType() default "list";
	
	/**
	 * Specifies the full class name of the items in the set
	 * @return
	 */
	String groupItemClass() default "";
	
	int precision() default 0;
	
	/**
	 * This is used to form multi and encapsulation sentences to make
	 * sure each sentence does not exceed the 80 char length limit. As well
	 * as for fields that require a specific number of characters, such as 2 chars
	 * for month, etc.
	 * @return
	 */
	int fieldWidth() default 1;

	/**
	 * One can use this field to indicate a default value when the field is left empty
	 * @return
	 */
	String defaultValue() default "";
	
	/**
	 * This is used for sentences that come with group items. When
	 * group items are involved, and there are fields after the group
	 * items to be parsed, this field need to be specified to calculate
	 * the number of items that can go into the group.
	 * @return
	 */
	int reservedSegments() default 0;
	
	boolean isIgnoredInReconstruction() default false;
}
