package com.liferay.commerce.demo.partnumber.checkout;

import com.liferay.commerce.constants.CommerceCheckoutWebKeys;
import com.liferay.commerce.demo.partnumber.checkout.display.context.PartNumberCheckoutStepDisplayContext;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.order.CommerceOrderHttpHelper;
import com.liferay.commerce.product.option.CommerceOptionValueHelper;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.commerce.service.CommerceOrderItemLocalService;
import com.liferay.commerce.util.BaseCommerceCheckoutStep;
import com.liferay.commerce.util.CommerceCheckoutStep;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author jhanda
 */
@Component(
        immediate = true,
        property = {
                "commerce.checkout.step.name=" + PartNumberCheckoutStep.NAME,
                "commerce.checkout.step.order:Integer=21"
        },
        service = CommerceCheckoutStep.class
)
public class PartNumberCheckoutStep extends BaseCommerceCheckoutStep {

    public static final String NAME = "part-number-checkout-step";

    @Override
    public String getLabel(Locale locale) {
        ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
                "content.Language", locale, getClass());

        return LanguageUtil.get(resourceBundle, "internal-part-number");
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        _log.error("In the process Action");
//        CommerceOrder commerceOrder = (CommerceOrder)actionRequest.getAttribute(CommerceCheckoutWebKeys.COMMERCE_ORDER);
//        List<CommerceOrderItem> commerceOrderItems = commerceOrder.getCommerceOrderItems();
//        for (CommerceOrderItem commerceOrderItem: commerceOrderItems){
//            String internalPartNumber = ParamUtil.getString(actionRequest, Long.toString(commerceOrderItem.getCommerceOrderItemId()));
//            if (!Validator.isBlank(internalPartNumber)){
//                commerceOrderItem.getExpandoBridge().setAttribute("internal-part-number", internalPartNumber);
//            }
//        }
    }

    @Override
    public void render(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse)
            throws Exception {

        PartNumberCheckoutStepDisplayContext partNumberCheckoutStepDisplayContext =
                new PartNumberCheckoutStepDisplayContext(_commerceChannelLocalService,
                        _commerceOptionValueHelper, _commerceOrderHttpHelper, _cpInstanceHelper, httpServletRequest);

        httpServletRequest.setAttribute(
                "partNumberCheckoutStepDisplayContext", partNumberCheckoutStepDisplayContext);

        _jspRenderer.renderJSP(
                _servletContext, httpServletRequest, httpServletResponse,
                "/part_number_form.jsp");
    }

    private static final Log _log = LogFactoryUtil.getLog(
            PartNumberCheckoutStep.class);

    @Reference
    private JSPRenderer _jspRenderer;

    @Reference(
            target = "(osgi.web.symbolicname=com.liferay.commerce.demo.partnumber.checkout)"
    )
    private ServletContext _servletContext;

    @Reference
    private CommerceChannelLocalService _commerceChannelLocalService;

    @Reference
    private CommerceOrderItemLocalService _commerceOrderItemLocalServce;

    @Reference
    private CommerceOptionValueHelper _commerceOptionValueHelper;

    @Reference
    private CommerceOrderHttpHelper _commerceOrderHttpHelper;

    @Reference
    private CPInstanceHelper _cpInstanceHelper;

}