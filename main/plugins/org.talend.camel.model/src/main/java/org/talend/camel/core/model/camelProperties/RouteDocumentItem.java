/**
 */
package org.talend.camel.core.model.camelProperties;

import org.talend.core.model.properties.DocumentationItem;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Route Document Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.camel.core.model.camelProperties.RouteDocumentItem#getBindingExtension <em>Binding Extension</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.camel.core.model.camelProperties.CamelPropertiesPackage#getRouteDocumentItem()
 * @model
 * @generated
 */
public interface RouteDocumentItem extends DocumentationItem {
	/**
	 * Returns the value of the '<em><b>Binding Extension</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Binding Extension</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Binding Extension</em>' attribute.
	 * @see #setBindingExtension(String)
	 * @see org.talend.camel.core.model.camelProperties.CamelPropertiesPackage#getRouteDocumentItem_BindingExtension()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
	String getBindingExtension();

	/**
	 * Sets the value of the '{@link org.talend.camel.core.model.camelProperties.RouteDocumentItem#getBindingExtension <em>Binding Extension</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Binding Extension</em>' attribute.
	 * @see #getBindingExtension()
	 * @generated
	 */
	void setBindingExtension(String value);

} // RouteDocumentItem
