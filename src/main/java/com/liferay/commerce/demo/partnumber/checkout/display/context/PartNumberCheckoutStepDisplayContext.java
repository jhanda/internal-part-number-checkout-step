package com.liferay.commerce.demo.partnumber.checkout.display.context;

import com.liferay.commerce.constants.CommerceCheckoutWebKeys;
import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.order.CommerceOrderHttpHelper;
import com.liferay.commerce.product.option.CommerceOptionValueHelper;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.product.util.CPInstanceHelper;

import javax.servlet.http.HttpServletRequest;

public class PartNumberCheckoutStepDisplayContext {

    public PartNumberCheckoutStepDisplayContext(CommerceChannelLocalService commerceChannelLocalService,
                                                CommerceOptionValueHelper commerceOptionValueHelper,
                                                CommerceOrderHttpHelper commerceOrderHttpHelper,
                                                CPInstanceHelper cpInstanceHelper,
                                                HttpServletRequest httpServletRequest) {
        _commerceChannelLocalService = commerceChannelLocalService;
        _commerceOptionValueHelper = commerceOptionValueHelper;
        _commerceOrderHttpHelper = commerceOrderHttpHelper;
        _cpInstanceHelper = cpInstanceHelper;
        _commerceContext = (CommerceContext)httpServletRequest.getAttribute(CommerceWebKeys.COMMERCE_CONTEXT);
        _commerceOrder = (CommerceOrder)httpServletRequest.getAttribute(CommerceCheckoutWebKeys.COMMERCE_ORDER);
    }

    public CommerceChannelLocalService getCommerceChannelLocalService() {
        return _commerceChannelLocalService;
    }

    public CommerceContext getCommerceContext() {
        return _commerceContext;
    }

    public CommerceOptionValueHelper getCommerceOptionValueHelper() {
        return _commerceOptionValueHelper;
    }

    public CommerceOrder getCommerceOrder() {
        return _commerceOrder;
    }

    public CommerceOrderHttpHelper getCommerceOrderHttpHelper() {
        return _commerceOrderHttpHelper;
    }

    public String getCommerceOrderItemThumbnailSrc(
            CommerceOrderItem commerceOrderItem)
            throws Exception {

        return _cpInstanceHelper.getCPInstanceThumbnailSrc(
                commerceOrderItem.getCPInstanceId());
    }

    public String getInternalPartNumber(CommerceOrderItem commerceOrderItem){
        return (String) commerceOrderItem.getExpandoBridge().getAttribute("internal-part-number");
    }

    private final CommerceChannelLocalService _commerceChannelLocalService;
    private final CommerceContext _commerceContext;
    private final CommerceOptionValueHelper _commerceOptionValueHelper;
    private final CommerceOrder _commerceOrder;
    private final CommerceOrderHttpHelper _commerceOrderHttpHelper;
    private final CPInstanceHelper _cpInstanceHelper;
}
