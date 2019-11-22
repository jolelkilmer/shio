/*
 * Copyright (C) 2016-2019 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.viglet.shiohara.onstartup.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.system.ShConfigVar;
import com.viglet.shiohara.persistence.repository.system.ShConfigVarRepository;

@Component
public class ShConfigVarOnStartup {
	public static final String FIRST_TIME_PATH = "/system";
	public static final String FIRST_TIME_NAME = "FIRST_TIME";
	
	@Autowired
	private ShConfigVarRepository shConfigVarRepository;

	public void createDefaultRows() {

		ShConfigVar shConfigVar = new ShConfigVar();

		if (!shConfigVarRepository.existsByPathAndName(FIRST_TIME_PATH, FIRST_TIME_NAME)) {

			shConfigVar.setPath(FIRST_TIME_PATH);
			shConfigVar.setName(FIRST_TIME_NAME);
			shConfigVar.setValue("true");
			shConfigVarRepository.save(shConfigVar);

			shConfigVar = new ShConfigVar();
		
			shConfigVar.setPath("/email");
			shConfigVar.setName("HOST");
			shConfigVar.setValue("localhost");

			shConfigVarRepository.save(shConfigVar);
		}
	}

}
