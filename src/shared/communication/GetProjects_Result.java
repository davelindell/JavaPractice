package shared.communication;

import java.util.List;

import shared.models.Project;

public class GetProjects_Result {
	private List<Project> project_info;
	
	GetProjects_Result(List<Project> project_info) {
		this.project_info = project_info;
	}

	
}
