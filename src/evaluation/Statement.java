package evaluation;

public class Statement {

	public String provenance;
	public String category;
	public String statement;
	public int quality;
	private String logicalForm;
	
	public Statement(String provenance, String category, String statement, int quality ) {
		this.provenance = provenance;
		this.category = category;
		this.statement = statement;
		this.quality = quality;
	}

	public String getLogicalForm() {
		return logicalForm;
	}

	public void setLogicalForm(String logicalForm) {
		this.logicalForm = logicalForm;
	}
	
	
	
	
}

