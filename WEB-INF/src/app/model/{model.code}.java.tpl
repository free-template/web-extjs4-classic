package app.model;

import java.sql.SQLException;
import net.rails.active_record.exception.RecordNotFoundException;
import net.rails.ext.AbsGlobal;
import app.helper.${model.code}Helper;

public final class ${model.code} extends ${model.code}Helper {
	
	public ${model.code}(AbsGlobal g) {
		super(g);
	}

	public ${model.code}(AbsGlobal g, Object id) throws SQLException,
			RecordNotFoundException {
		super(g, id);
	}

}
