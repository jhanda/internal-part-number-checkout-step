<%@ taglib prefix="aui" uri="http://liferay.com/tld/aui" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://liferay.com/tld/commerce-ui" prefix="commerce-ui" %>
<%@ taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.commerce.demo.partnumber.checkout.display.context.PartNumberCheckoutStepDisplayContext" %>
<%@ page import="com.liferay.commerce.model.CommerceOrder" %>
<%@ page import="com.liferay.commerce.model.CommerceOrderItem" %>
<%@ page import="com.liferay.commerce.product.model.CPDefinition" %>
<%@ page import="com.liferay.portal.kernel.util.Constants" %>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="java.util.List" %>

<liferay-frontend:defineObjects />

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
    PartNumberCheckoutStepDisplayContext partNumberCheckoutStepDisplayContext =
            (PartNumberCheckoutStepDisplayContext)request.getAttribute("partNumberCheckoutStepDisplayContext");
    CommerceOrder commerceOrder = partNumberCheckoutStepDisplayContext.getCommerceOrder();
    List<CommerceOrderItem> commerceOrderItems = commerceOrder.getCommerceOrderItems();
%>

<portlet:actionURL name="savePartNumber" var="savePartNumberActionURL" />

<aui:form action="<%= savePartNumberActionURL %>" data-senna-off="true" method="post" name="fm">

    <aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

<div class="commerce-checkout-summary-body" id="<portlet:namespace />entriesContainer">
    <liferay-ui:search-container
            cssClass="list-group-flush"
            id="commerceOrderItems"
    >
        <liferay-ui:search-container-results
                results="<%= commerceOrder.getCommerceOrderItems() %>"
        />

        <liferay-ui:search-container-row
                className="com.liferay.commerce.model.CommerceOrderItem"
                cssClass="entry-display-style"
                keyProperty="CommerceOrderItemId"
                modelVar="commerceOrderItem"
        >

            <%
                CPDefinition cpDefinition = commerceOrderItem.getCPDefinition();
            %>

            <liferay-ui:search-container-column-image
                    cssClass="thumbnail-section"
                    name="image"
                    src="<%= partNumberCheckoutStepDisplayContext.getCommerceOrderItemThumbnailSrc(commerceOrderItem) %>"
            />

            <liferay-ui:search-container-column-text
                    cssClass="autofit-col-expand"
                    name="product"
            >
                <div class="description-section">
                    <div class="list-group-title">
                        <%= HtmlUtil.escape(cpDefinition.getName(themeDisplay.getLanguageId())) %>
                    </div>
                </div>
            </liferay-ui:search-container-column-text>

            <liferay-ui:search-container-column-text
                    name="quantity"
            >
                <div class="quantity-section">
                    <span class="commerce-quantity"><%= commerceOrderItem.getQuantity() %></span><span class="inline-item-after">x</span>
                </div>
            </liferay-ui:search-container-column-text>

            <liferay-ui:search-container-column-text
                    name="internal-part-number"
            >
                <div class="part-number-section">
                    <aui:input id="<%= Long.toString(commerceOrderItem.getCommerceOrderItemId()) %>"  label="" name="<%= Long.toString(commerceOrderItem.getCommerceOrderItemId()) %>" value="<%= partNumberCheckoutStepDisplayContext.getInternalPartNumber(commerceOrderItem)%>" />
                </div>
            </liferay-ui:search-container-column-text>

        </liferay-ui:search-container-row>

        <liferay-ui:search-iterator
                displayStyle="list"
                markupView="lexicon"
                paginate="<%= false %>"
        />
    </liferay-ui:search-container>

</aui:form>
</div>

