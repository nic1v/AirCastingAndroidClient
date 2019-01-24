/**
    AirCasting - Share your Air!
    Copyright (C) 2011-2012 HabitatMap, Inc.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

    You can contact the authors by email at <info@habitatmap.org>
*/
package pl.llp.aircasting.networking.schema;

import com.google.gson.annotations.Expose;

/**
 * Created by IntelliJ IDEA.
 * User: obrok
 * Date: 11/23/11
 * Time: 5:07 PM
 */
public class UserInfo {
    @Expose private String email;
    @Expose private String username;
    @Expose private String authentication_token;
    @Expose private String password;

    public UserInfo(String email, String username, String authentication_token) {
        this.email = email;
        this.username = username;
        this.authentication_token = authentication_token;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthenticationToken() {
        return authentication_token;
    }

    public String getPassword() {
        return password;
    }
}


