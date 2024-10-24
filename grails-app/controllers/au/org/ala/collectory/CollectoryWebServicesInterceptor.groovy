/*
 * Copyright (C) 2022 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

package au.org.ala.collectory

import org.apache.http.HttpStatus


class CollectoryWebServicesInterceptor {

    CollectoryAuthService collectoryAuthService

    CollectoryWebServicesInterceptor(){
        match(controller: 'data', action: "saveEntity")
        match(controller:'data', action:"syncGBIF")
        match('controller':'data', action: 'updateContact')
        match('controller':'data', action: 'updateContactFor')
        match('controller':'data', action: 'contacts')
        match(controller:'gbif', action:"scan")
        match(controller:'ipt', action:"scan")

    }
    boolean before() {
        // set default role requirement for protected ROLE_EDITOR as the same info is only available to ROLE_EDITOR via the UI.
        String[] requiredRoles = [grailsApplication.config.ROLE_EDITOR]

        // set gbifRegistrationRole role requirement for GBIF  operations
        if(controllerName == 'gbif' || actionName == 'syncGBIF'){
            requiredRoles = [grailsApplication.config.gbifRegistrationRole]
        }

        // set ROLE_ADMIN role requirement for certain controllers and actions as per admin UI
        if( controllerName == 'ipt'){
            requiredRoles = [grailsApplication.config.ROLE_ADMIN]
        }

        if (collectoryAuthService.isAuthorisedWsRequest(params, request, response, requiredRoles, null)) {
            return true
        }
        log.warn("Denying access to $actionName from remote addr: ${request.remoteAddr}, remote host: ${request.remoteHost}")
        response.sendError(HttpStatus.SC_UNAUTHORIZED)
        return false
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
