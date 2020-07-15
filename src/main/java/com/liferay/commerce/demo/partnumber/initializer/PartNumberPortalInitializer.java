package com.liferay.commerce.demo.partnumber.initializer;

import com.liferay.commerce.demo.partnumber.constants.PartNumberConstants;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.expando.kernel.exception.DuplicateColumnNameException;
import com.liferay.expando.kernel.exception.NoSuchTableException;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoColumnLocalServiceUtil;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.UnicodeProperties;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@Component(immediate = true, service = PartNumberPortalInitializer.class)
public class PartNumberPortalInitializer {

    @Activate
    public void activate() {
        List<Company> companies = _companyLocalService.getCompanies();

        for (Company company : companies) {
            try {
                _addExpandoColumns(company.getCompanyId());
            }
            catch (PortalException e) {
                _log.error(e.getMessage(), e);
            }
        }
    }

    private void _addExpandoColumns(long companyId) throws PortalException{

        ExpandoTable table = null;
        try {
            table = _expandoTableLocalService.getTable(
                        companyId, CommerceOrderItem.class.getName(), "CUSTOM_FIELDS");
        }
        catch(NoSuchTableException nste) {
            table = _expandoTableLocalService.addTable(
                        companyId, CommerceOrderItem.class.getName(), "CUSTOM_FIELDS");
        }

        ExpandoColumn column = null;
        long tableId = table.getTableId();

        try {
            column = _expandoColumnLocalService.addColumn(
                    tableId, PartNumberConstants.INTERNAL_PART_NUMBER_LABEL,
                    ExpandoColumnConstants.STRING);

            UnicodeProperties properties = new UnicodeProperties();

            properties.setProperty(ExpandoColumnConstants.INDEX_TYPE,
                    String.valueOf(ExpandoColumnConstants.INDEX_TYPE_KEYWORD));
            properties.setProperty(ExpandoColumnConstants.PROPERTY_LOCALIZE_FIELD_NAME, "true");
            column.setTypeSettingsProperties(properties);
            ExpandoColumnLocalServiceUtil.updateExpandoColumn(column);

            Role userRole = _roleLocalService.getRole(
                    companyId, RoleConstants.USER);
            String resourceId = ExpandoColumn.class.getName();
            String primKey = String.valueOf(column.getColumnId());
            String[] actionKeys = {ActionKeys.VIEW, ActionKeys.UPDATE};

            _grantPermissions(
                    companyId, userRole.getRoleId(), resourceId,
                    ResourceConstants.SCOPE_INDIVIDUAL, primKey, actionKeys);
        }
        catch(DuplicateColumnNameException dcne) {
            column = _expandoColumnLocalService.getColumn(
                    tableId, PartNumberConstants.INTERNAL_PART_NUMBER_LABEL);
        }
    }

    private void _grantPermissions(
            long companyId, long roleId, String resourceId, int scope,
            String primKey, String[] actionKeys)
            throws PortalException {

        for (int i = 0; i < actionKeys.length; i++) {
            _resourceActionLocalService.checkResourceActions(
                    resourceId, Arrays.asList(actionKeys));
            _resourcePermissionLocalService.setResourcePermissions(
                    companyId, resourceId, scope, primKey, roleId, actionKeys);
        }
    }
    @Reference
    private CompanyLocalService _companyLocalService;

    private static final Logger _log = LoggerFactory.getLogger(
            PartNumberPortalInitializer.class);

    @Reference
    private ExpandoColumnLocalService _expandoColumnLocalService;

    @Reference
    private ExpandoTableLocalService _expandoTableLocalService;

    @Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
    private ModuleServiceLifecycle _portalInitialized;

    @Reference
    private ResourceActionLocalService _resourceActionLocalService;

    @Reference
    private ResourcePermissionLocalService _resourcePermissionLocalService;

    @Reference
    private RoleLocalService _roleLocalService;

}
