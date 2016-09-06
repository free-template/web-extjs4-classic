package app.helper;

import app.helper.ApplicationHelper;
import java.sql.SQLException;
import net.rails.active_record.exception.RecordNotFoundException;
import net.rails.ext.AbsGlobal;

/**
 * $model.name
 * <span>$model.comment</span>
 */
public class ${model.code}Helper extends ApplicationHelper {

	public ${model.code}Helper(AbsGlobal g) {
		super(g);
	}

	public ${model.code}Helper(AbsGlobal g,Object id)  throws SQLException, RecordNotFoundException {
		super(g,id);
	}
	
	
#foreach($field in $model.knownFields)
	$field.generateSetGetMethod()
#end
}