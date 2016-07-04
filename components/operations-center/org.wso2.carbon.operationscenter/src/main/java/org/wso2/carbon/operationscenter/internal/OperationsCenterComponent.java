
/*
 * Copyright 2015 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.operationscenter.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.operationscenter.util.OperationsCenterDataHolder;
import org.wso2.carbon.operationscenter.util.OperationsCenterDbUtils;
import org.wso2.carbon.ndatasource.core.DataSourceService;

/**
 * @scr.component name="operationscenter.component" immediate="true"
 * @scr.reference name="datasources.service"
 * interface="org.wso2.carbon.ndatasource.core.DataSourceService"
 * cardinality="1..1" policy="dynamic"
 * bind="setDataSourceService" unbind="unsetDataSourceService"
 */
public class OperationsCenterComponent {
    private static Log log = LogFactory.getLog(OperationsCenterComponent.class);

    protected void activate(ComponentContext componentContext) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("OperationsCenterComponent activation started");
            }

            OperationsCenterDbUtils.initialize();

            if (log.isDebugEnabled()) {
                log.debug("OperationsCenterComponent activation complete");
            }
        } catch (Throwable throwable) {
            log.error("Error while stating OperationsCenterComponent", throwable);
        }
    }

    protected void deactivate(ComponentContext componentContext) {
        if (log.isDebugEnabled()) {
            log.debug("Deactivated OperationsCenterComponent");
        }
    }

    protected void setDataSourceService(DataSourceService dataSourceService){
        OperationsCenterDataHolder.getInstance().setDataSourceService(dataSourceService);
    }

    protected void unsetDataSourceService(DataSourceService dataSourceService){
        OperationsCenterDataHolder.getInstance().setDataSourceService(null);
    }
}
