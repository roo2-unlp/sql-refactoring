
public class RenameAlias extends Refactoring {

	private String alias;
	
	public void renameAlias(String alias) {
		this.alias = alias;
	}
	public boolean aliasExist() {
		return true;
	}
	public void newAliasNotExist() {
		
	}
	@Override
	protected boolean checkPreconditions(String text) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected String transform(String text) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected boolean checkPostconditions(String text) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
}
