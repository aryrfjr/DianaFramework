package org.dianaframework.swing.validators;

import com.opensymphony.xwork.validator.ValidationException;
import com.opensymphony.xwork.validator.validators.FieldValidatorSupport;


/**
 * <!-- START SNIPPET: javadoc -->
 * RequiredFieldValidator checks if the specified field is a number.
 * <!-- END SNIPPET: javadoc -->
 * <p/>
 *
 *
 * <!-- START SNIPPET: parameters -->
 * <ul>
 * 		<li>fieldName - field name if plain-validator syntax is used, not needed if field-validator syntax is used</li>
 * </ul>
 * <!-- END SNIPPET: parameters -->
 *
 *
 * <pre>
 * <!-- START SNIPPET: example -->
 * 	   &lt;validators&gt;
 *
 *         &lt;!-- Plain Validator Syntax --&gt;
 *         &lt;validator type="numberrequired"&gt;
 *             &lt;param name="fieldName"&gt;number&lt;/param&gt;
 *             &lt;message&gt;number must be a numeric value&lt;/message&gt;
 *         &lt;/validator&gt;
 *
 *
 *         &lt;!-- Field Validator Syntax --&gt;
 *         &lt;field name="number"&gt;
 *             &lt;field-validator type="numberrequired"&gt;
 *             	   &lt;message&gt;number must not be a numeric value&lt;/message&gt;
 *             &lt;/field-validator&gt;
 *         &lt;/field&gt;
 *
 *     &lt;/validators&gt;
 * <!-- END SNIPPET: example -->
 * </pre>
 *
 * @author Ary Junior<a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 * @version $Id: NumberRequiredValidator.java 1594 2007-02-07 11:12:15Z bruno $
 *
 */
public class NumberRequiredValidator extends FieldValidatorSupport {
    
    public void validate(Object object) throws ValidationException {
        String fieldName = getFieldName();
        Object value = this.getFieldValue(fieldName, object);
        if (!isNumeric((String)value)) {
            addFieldError(fieldName, object);
        }
    }
    
    private boolean isNumeric(final String s) {
        final char[] numbers = s.toCharArray();
        for (int x = 0; x < numbers.length; x++) {
            final char c = numbers[x];
            if ((c >= '0' && c <= '9') || (c == '-')) continue;
            return false; // invalid
        }
        return true; // valid
    }
    
}
