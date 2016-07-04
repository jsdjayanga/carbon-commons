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
package org.wso2.carbon.operationscenter.ui;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.operationscenter.stub.*;
import org.wso2.carbon.operationscenter.stub.types.beans.Environment;
import org.wso2.carbon.operationscenter.stub.types.beans.Server;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class OperationsCenterAdminClient {
    private static Log log = LogFactory.getLog(OperationsCenterAdminClient.class);
    private static final String BUNDLE = "org.wso2.carbon.operationscenter.ui.i18n.Resources";
    private ResourceBundle bundle;
    public OperationsCenterAdminStub operationsCenterAdminStub;

    public OperationsCenterAdminClient(String cookie,
                                       String backendServerURL,
                                       ConfigurationContext configurationContext,
                                       Locale locale) throws AxisFault {
        String serviceURL = backendServerURL + "OperationsCenterAdmin";
        bundle = ResourceBundle.getBundle(BUNDLE, locale);

        operationsCenterAdminStub = new OperationsCenterAdminStub(configurationContext, serviceURL);
        ServiceClient client = operationsCenterAdminStub._getServiceClient();
        Options option = client.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);
        option.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
    }

    public Environment[] getEnvironmentsList() {
        try {
            return operationsCenterAdminStub.getAllEnvironments();
        } catch (java.lang.Exception e) {
            log.error(bundle.getString("cannot.get.service.data"), e);
        }
        return null;
    }

    public Server[] getServersList() {
        try {
            return operationsCenterAdminStub.getAllServers();
        } catch (java.lang.Exception e) {
            log.error(bundle.getString("cannot.get.service.data"), e);
        }
        return null;
    }
}
