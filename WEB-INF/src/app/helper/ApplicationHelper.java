package app.helper;

import java.sql.SQLException;
import net.rails.active_record.ActiveRecord;
import net.rails.active_record.exception.RecordNotFoundException;
import net.rails.ext.AbsGlobal;
import net.rails.support.Support;

public class ApplicationHelper extends ActiveRecord {
	
	public ApplicationHelper(AbsGlobal g) {
		super(g);
		setId(Support.code().id());
	}

	public ApplicationHelper(AbsGlobal g, Object id) throws SQLException,
			RecordNotFoundException {
		super(g, id);
	}
	
	public String getCreatedAtText(){
		return g.timestamp2text(getTimestamp("created_at"));
	}
	
	public String getUpdatedAtText(){
		return g.timestamp2text(getTimestamp("updated_at"));
	}
	
	public String getDeletedText(){
		return g.timestamp2text(getTimestamp("deleted_at"));
	}

	public String getCreatedUserId() {
		return getString("created_user_id");
	}

	public String getUpdatedUserId() {
		return getString("updated_user_id");
	}
	
	public String getDeletedUserId() {
		return getString("deleted_user_id");
	}

	public java.sql.Timestamp getCreatedAt() {
		return getTimestamp("created_at");
	}

	public java.sql.Timestamp getUpdatedAt() {
		return getTimestamp("updated_at");
	}

	public java.sql.Timestamp getDeletedAt() {
		return getTimestamp("deleted_at");
	}

	public Boolean isDeleted() {
		return isBoolean("deleted");
	}

}
