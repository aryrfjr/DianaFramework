package org.dianaframework.view;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.Serializable;
import org.dianaframework.action.ActionReference;

/**
 *
 * Classe que representa um campo de formulario.
 *
 * @version $Id: FormField.java 2142 2007-03-07 21:00:46Z aryjunior $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class FormField implements Serializable {
public static String UNDEFINED = "UNDEFINED";
public static String DATE = "DATE";
public static String IMAGE = "IMAGE";
public static String GENERIC = "GENERIC";
private String type = UNDEFINED;
private String name;
private CollectionModel model;
private Object value;
private ActionReference keyPressAction;
private ActionReference selectionAction;
private ActionReference focusGainedAction;
private ActionReference changeAction;
private String strEnabled = "true";
private String strVisible = "true";
private boolean enabled = true;
private boolean visible = true;
private boolean ignoreState = false;
private boolean reload = true;
private boolean ignoreValue = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CollectionModel getModel() {
        return model;
    }

    public void setModel(CollectionModel model) {
        this.model = model;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ActionReference getKeyPressAction() {
        return keyPressAction;
    }

    public void setKeyPressAction(ActionReference keyPressAction) {
        this.keyPressAction = keyPressAction;
    }

    public ActionReference getSelectionAction() {
        return selectionAction;
    }

    public void setSelectionAction(ActionReference selectionAction) {
        this.selectionAction = selectionAction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ActionReference getFocusGainedAction() {
        return focusGainedAction;
    }

    public void setFocusGainedAction(ActionReference focusGainedAction) {
        this.focusGainedAction = focusGainedAction;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ActionReference getChangeAction() {
        return changeAction;
    }

    public void setChangeAction(ActionReference changeAction) {
        this.changeAction = changeAction;
    }
    
    public byte[] getBytesFromValue() {
        if (getType().equals(FormField.IMAGE)) {
            
        }
        return null;
    }

    public boolean isIgnoreState() {
        return ignoreState;
    }

    public void setIgnoreState(boolean ignoreState) {
        this.ignoreState = ignoreState;
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(String reload) {
        this.reload = reload.equalsIgnoreCase("YES");
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getStrEnabled() {
        return strEnabled;
    }

    public void setStrEnabled(String strEnabled) {
        setEnabled(strEnabled.equalsIgnoreCase("true"));
        this.strEnabled = strEnabled;
    }

    public String getStrVisible() {
        setVisible(strEnabled.equalsIgnoreCase("true"));
        return strVisible;
    }

    public void setStrVisible(String strVisible) {
        this.strVisible = strVisible;
    }

    public boolean isIgnoreValue() {
        return ignoreValue;
    }

    public void setIgnoreValue(boolean ignoreValue) {
        this.ignoreValue = ignoreValue;
    }

}
