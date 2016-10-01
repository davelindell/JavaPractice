package shared.communication;

import java.util.List;

import shared.models.Project;

public class GetProjects_Result {
	private List<Project> project_info;
	
	public GetProjects_Result(List<Project> project_info) {
		this.project_info = project_info;
	}

	public List<Project> getProject_info() {
		return project_info;
	}

	public void setProject_info(List<Project> project_info) {
		this.project_info = project_info;
	}

	@Override
	public String toString() {
		if (project_info == null)
			return "FAILED\n";
		else {
			String result = "";
			for (Project proj : project_info) {
				result += Integer.toString(proj.getProject_id()) + "\n" + proj.getProject_title() + "\n";
			}
			return result;
		}
	}
	
}
