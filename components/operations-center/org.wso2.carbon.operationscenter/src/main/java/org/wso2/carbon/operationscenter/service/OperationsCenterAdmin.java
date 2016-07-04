
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

package org.wso2.carbon.operationscenter.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.AbstractAdmin;
import org.wso2.carbon.operationscenter.bean.Environment;
import org.wso2.carbon.operationscenter.bean.Server;
import org.wso2.carbon.operationscenter.util.OperationsCenterDbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OperationsCenterAdmin extends AbstractAdmin {
    private static Log log = LogFactory.getLog(OperationsCenterAdmin.class);

    public String createEnvironment(String environmentName, String environmentDescription) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Creating an environment [Name=" + environmentName + ", Description=" + environmentDescription + "]");
        }

        Connection connection = OperationsCenterDbUtils.getConncetion();
        String query = "INSERT" +
                " INTO OC_ENVIRONMENT (NAME, DESCRIPTION)" +
                " VALUES (?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"ID"});
        preparedStatement.setString(1, environmentName);
        preparedStatement.setString(2, environmentDescription);
        preparedStatement.executeUpdate();

        int environmentID = -1;
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            environmentID = resultSet.getInt(1);
        }

        return Integer.toString(environmentID);
    }

    public String createServer(String serverMetaId, String environmentId, String serverNickname, String ip, String location) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Creating an server [ServerMetaId=" + serverMetaId + ", EnvironmentId=" + environmentId +
                    ", ServerNickname=" + serverNickname + ", IP=" + ip + ", Location=" + location + "]");
        }

        Connection connection = OperationsCenterDbUtils.getConncetion();
        String query = "INSERT" +
                " INTO OC_SERVER_INSTANCE (META_ID, ENVIRONMENT_ID, NICKNAME, IP, LOCATION)" +
                " VALUES (?,?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"ID"});
        preparedStatement.setString(1, serverMetaId);
        preparedStatement.setString(2, environmentId);
        preparedStatement.setString(3, serverNickname);
        preparedStatement.setString(4, ip);
        preparedStatement.setString(5, location);
        preparedStatement.executeUpdate();

        int serverId = -1;
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            serverId = resultSet.getInt(1);
        }

        return Integer.toString(serverId);
    }

    public void addServerCommand(String serverId, String actionId, String command) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Adding server command [ServerId=" + serverId + ", ActionId=" + actionId + ", Command=" + command + "]");
        }

        Connection connection = OperationsCenterDbUtils.getConncetion();
        String query = "INSERT" +
                " INTO OC_SERVER_COMMAND (SERVER_ID, ACTION_ID, COMMAND)" +
                " VALUES (?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"ID"});
        preparedStatement.setString(1, serverId);
        preparedStatement.setString(2, actionId);
        preparedStatement.setString(3, command);
        preparedStatement.executeUpdate();
    }

    public Environment[] getAllEnvironments() throws Exception{
        List<Environment> environmentList = new ArrayList<>();

        Connection connection = OperationsCenterDbUtils.getConncetion();
        String query = "SELECT * FROM OC_ENVIRONMENT";

        PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"ID", "NAME", "DESCRIPTION"});
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            environmentList.add(new Environment(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
        }

        return environmentList.toArray(new Environment[environmentList.size()]);
    }

    public Server[] getAllServers() throws Exception{
        List<Server> ServerList = new ArrayList<>();

        Connection connection = OperationsCenterDbUtils.getConncetion();
        String query = "SELECT OC.OC_SERVER_INSTANCE.ID, OC.META_SERVER.NAME AS SERVER, OC.OC_ENVIRONMENT.NAME AS ENVIRONMENT, OC.OC_SERVER_INSTANCE.NICKNAME, OC.OC_SERVER_INSTANCE.IP, OC.OC_SERVER_INSTANCE.LOCATION  FROM OC.OC_SERVER_INSTANCE, OC.OC_ENVIRONMENT, OC.META_SERVER WHERE OC_SERVER_INSTANCE.META_ID = META_SERVER.ID AND OC_SERVER_INSTANCE.ENVIRONMENT_ID = OC_ENVIRONMENT.ID";

        PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"ID", "SERVER", "ENVIRONMENT", "NICKNAME", "IP", "LOCATION"});
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            ServerList.add(new Server(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)));
        }

        return ServerList.toArray(new Server[ServerList.size()]);
    }

    public void startServer(String serverId) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Starting server [ServerId=" + serverId + "]");
        }

        Connection connection = OperationsCenterDbUtils.getConncetion();
        String query = "SELECT OC.OC_SERVER_INSTANCE.IP VALUES (?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"IP"});
        preparedStatement.setString(1, serverId);
        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            log.info("Starting server [ServerId=" + serverId + "]");
        }
    }
}
