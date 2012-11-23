package org.jboss.as.quickstarts.kitchensink.util;

import java.util.Locale;

import javax.enterprise.inject.Alternative;
import javax.faces.context.FacesContext;

import org.apache.deltaspike.core.api.message.LocaleResolver;
import org.apache.deltaspike.core.api.message.MessageContext;

/**
 * This class uses {@link FacesContext} to discover what is the Locale that should be used on {@link MessageContext}
 * 
 * We used the {@link Alternative} annotation to avoid conflicts with {@link DefaultLocaleResolver}
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@Alternative
public class KitchensinkLocaleResolver implements LocaleResolver {

    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.deltaspike.core.api.message.LocaleResolver#getLocale()
     */
    @Override
    public Locale getLocale() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

}
