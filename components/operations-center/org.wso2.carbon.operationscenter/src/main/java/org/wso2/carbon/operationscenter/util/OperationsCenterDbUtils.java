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

package org.wso2.carbon.operationscenter.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.ndatasource.common.DataSourceException;
import org.wso2.carbon.ndatasource.core.CarbonDataSource;
import org.wso2.carbon.operationscenter.exception.OperationsCenterDataSourceException;

import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.Connection;
import java.sql.SQLException;

public class OperationsCenterDbUtils {
    private static Log log = LogFactory.getLog(OperationsCenterDbUtils.class);
    private static final String OPERATIONS_CENTER_DATASOURCE = "OPERATIONS_CENTER_DATASOURCE";

    private static volatile DataSource dataSource = null;

    private OperationsCenterDbUtils() {
    }

    public static void initialize() throws OperationsCenterDataSourceException {
        if (dataSource != null) {
            return;
        }
        synchronized (OperationsCenterDbUtils.class) {
            if (dataSource == null) {
                Context context = null;
                try {
                    CarbonDataSource carbonDataSource = OperationsCenterDataHolder.getInstance().getDataSourceService()
                            .getDataSource(OPERATIONS_CENTER_DATASOURCE);
                    if (carbonDataSource != null) {
                        dataSource = (DataSource) carbonDataSource.getDSObject();
                    }
                } catch (DataSourceException e) {
                    throw new OperationsCenterDataSourceException("Exception occurred while creating DataSource", e);
                }

                setupOperationsCenterDb();

                if (log.isDebugEnabled()) {
                    log.debug("OperationsCenterDbUtils successfully initialized");
                }
            }
        }
    }

    public static Connection getConncetion() throws SQLException {
        if (dataSource != null) {
            return dataSource.getConnection();
        }
        throw new SQLException("DataSource is null");
    }

    private static void setupOperationsCenterDb() {
        // Check for system property "setup" and create the database
    }
}

