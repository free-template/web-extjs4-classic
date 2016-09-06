package app.controller.www.admin.service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import app.controller.www.admin.exception.NoPermissionException;
import app.helper.ApplicationHelper;
import net.rails.active_record.ActiveRecord;
import net.rails.active_record.exception.MessagesException;
import net.rails.ext.AbsGlobal;
import net.rails.ext.Json;
import net.rails.sql.query.Query;
import net.rails.sql.worker.FindWorker;

public class ActionService extends app.service.ApplicationService {

	public ActionService(AbsGlobal g) {
		super(g);
	}

	public int settingWaiter() {
		try {
			return 0;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return 0;
		}
	}

	protected boolean beforeQuery(Query q, String fun, boolean disc,
			String first, String last, Map<String, Object> otherParams)
			throws Exception {
		return true;
	}
	
	protected boolean beforeCreate(ApplicationHelper ar) throws Exception {
		return true;
	}
	
	protected boolean beforeUpdate(ApplicationHelper ar) throws Exception {
		return true;
	}
	
	protected boolean beforeRemove(ApplicationHelper ar) throws Exception {
		return true;
	}
	
	protected boolean beforeBelongsTo(ApplicationHelper ar) throws Exception {
		return true;
	}
	
	protected boolean beforeHasOne(ApplicationHelper ar) throws Exception {
		return true;
	}
	
	public int belongsToWaiter(ApplicationHelper ar,String belongName) {
		Json<String,Object> json = new Json<String,Object>();
		try{
			ApplicationHelper belongAr = ar.belongsTo(belongName);
			if(beforeBelongsTo(belongAr)){
				json.put("status", 1);
				json.put("name", belongAr.getClass().getSimpleName());
				json.put("data", belongAr);
				setStatement(json);
				return 1;
			}else{
				return 998;
			}			
		}catch(Exception e){
			log.error(e.getMessage(),e);
			return 0;
		}
	}
	
	public int hasOneWaiter(ApplicationHelper ar,String hasName) {
		Json<String,Object> json = new Json<String,Object>();
		try{
			ApplicationHelper hasOneAr = ar.hasOne(hasName);
			if(beforeHasOne(hasOneAr)){
				json.put("status", 1);
				json.put("name", hasOneAr.getClass().getSimpleName());
				json.put("data", hasOneAr);
				setStatement(json);
				return 1;
			}else{
				return 998;
			}			
		}catch(Exception e){
			log.error(e.getMessage(),e);
			return 0;
		}
	}
	
	public int hasManyWaiter(ApplicationHelper ar,String hasName) {
		try{
			setStatement(ar.hasMany(hasName));
			return 1;		
		}catch(Exception e){
			log.error(e.getMessage(),e);
			return 0;
		}
	}
	
	protected boolean beforeList(Query q) {
		return true;
	}
	
	protected boolean beforeBoxList(Query q,String query) {
		return true;
	}
	
	protected Json<String, Object> list(FindWorker fw) throws Exception {
		Json<String, Object> json = new Json<String, Object>();
		FindWorker cfw = fw.clone();
		Number total = 0;
		cfw.orders().clear();
		cfw.selects().clear();
		cfw.setLimit(null);
		cfw.setOffset(null);
		cfw.firsts().add("SELECT COUNT(*) AS total FROM (");
		cfw.lasts().add(") temptable");
		total = cfw.first().getNumber("total");
		List<ActiveRecord> data = null;
		if (total.longValue() > 0) {
			data = fw.find();
		} else{
			data = new ArrayList<ActiveRecord>();
		}
		json.put("total", total);
		json.put("data", data);
		return json;
	}

	protected Json<String, Object> find(FindWorker fw) throws Exception {
		Json<String, Object> json = new Json<String, Object>();
		json.put("status", 1);
		json.put("data", fw.find());
		return json;
	}

	protected Json<String, Object> first(FindWorker fw) throws Exception {
		Json<String, Object> json = new Json<String, Object>();
		ApplicationHelper ar = fw.first();
		if (ar == null) {
			json.put("status", -1);
			json.put("msg", g.t("common", "not_found"));
		} else {
			json.put("status", 1);
			json.put("data", ar);
		}
		return json;
	}

	protected Json<String, Object> query(FindWorker fw, String fun, boolean disc,
			String first, String last, Map<String, Object> otherParams)
			throws Exception {
		Json<String, Object> json = new Json<String, Object>();
		if (fun.equals("find")) {
			fw.setDistinct(disc);
			fw.firsts().add(first);
			fw.lasts().add(last);
			json = find(fw);
		} else if (fun.equals("first")) {
			fw.setLimit(null);
			fw.setOffset(null);
			fw.setDistinct(disc);
			fw.firsts().add(first);
			fw.lasts().add(last);
			json = first(fw);
		}
		return json;
	}

	public void throwNoPermission() throws NoPermissionException {
		throw new NoPermissionException(g.locale("common", "no_permission")
				.toString());
	}

	public Json<String, Object> getNoPermission() {
		Json<String, Object> json = new Json<String, Object>();
		json.put("status", 998);
		json.put("msg", g.locale("common", "no_permission").toString());
		return json;
	}
	
	@SuppressWarnings("unchecked")
	protected void evalQuery(Query q, String fun, List<Object> keyarrs)
			throws Exception {
		for (Object arrs : keyarrs) {
			Method method = null;
			List<String> arr = (List<String>) arrs;
			switch (arr.size()) {
			case 1:
				method = Query.class.getMethod(fun, String.class);
				method.invoke(q, arr.get(0));
				break;
			case 2:
				method = Query.class.getMethod(fun, String.class, String.class);
				method.invoke(q, arr.get(0), arr.get(1));
				break;
			case 3:
				method = Query.class.getMethod(fun, String.class, String.class,
						String.class);
				method.invoke(q, arr.get(0), arr.get(1), arr.get(2));
				break;
			default:
				break;
			}
		}
	}
	
	public int removeWaiter(ApplicationHelper ar) {
		try {
			if (beforeRemove(ar)) {
				boolean deleted = ar.delete();
				if(deleted){
					ar.refresh();
					setStatement(ar);
					return 1;
				}else{
					return 0;
				}
			} else {
				return 998;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return 0;
		}
	}
	
	public int updateWaiter(ApplicationHelper ar) throws MessagesException {
		try {
			if (beforeUpdate(ar)) {
				ar.onUpdate();
				ar.refresh();
				setStatement(ar);
				return 1;
			} else {
				return 998;
			}
		}catch (MessagesException e) {
			log.error(e.getMessage(), e);
			throw e;
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			return 0;
		}
	}
	
	public int createWaiter(ApplicationHelper ar) throws MessagesException{
		try {
			if (beforeCreate(ar)) {
				boolean created = ar.onCreate();
				if(created){
					ar.refresh();
					setStatement(ar);
					return 1;
				}else{
					return 0;
				}
			} else {
				return 998;
			}
		} catch (MessagesException e) {
			log.error(e.getMessage(), e);
			throw e;
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			return 0;
		}
	}

	public int queryWaiter(String fun, String from, String first, String last,
			Map<String, Object> ands, Map<String, Object> ors,
			Map<String, Object> otherParams, List<Object> selects,
			List<Object> as, List<Object> lefts, List<Object> inners,
			List<Object> rights, List<Object> counts, boolean disc,
			boolean select, boolean join, boolean group, boolean skipnil,
			Integer limit, Integer offset) {
		Json<String, Object> json = new Json<String, Object>();
		try {
			Query q = Query.from(g, from);
			evalQuery(q, "select", selects);
			evalQuery(q, "as", as);
			evalQuery(q, "left", lefts);
			evalQuery(q, "right", rights);
			evalQuery(q, "inner", inners);
			evalQuery(q, "count", counts);
			q.select(select);
			q.join(join);
			q.group(group);
			q.skipnil(skipnil);
			q.and(ands);
			q.or(ors);
			q.limit(limit);
			q.offset(offset);
			boolean next = beforeQuery(q, fun, disc, first, last, otherParams);
			if (next) {
				json = query(q.getWorker(), fun, disc, first, last, otherParams);
				setStatement(json);
				return 1;
			} else {
				return 998;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return 0;
		}
	}
	
	protected Json<String, Object> boxList(FindWorker fw) throws Exception {
		Json<String, Object> json = new Json<String, Object>();
		FindWorker cfw = fw.clone();
		Number total = 0;
		cfw.orders().clear();
		cfw.selects().clear();
		cfw.setLimit(null);
		cfw.setOffset(null);
		cfw.firsts().add("SELECT COUNT(*) AS total FROM (");
		cfw.lasts().add(") temptable");
		total = cfw.first().getNumber("total");
		List<ActiveRecord> data = null;
		if (total.longValue() > 0) {
			data = fw.find();
		} else{
			data = new ArrayList<ActiveRecord>();
		}
		json.put("total", total);
		json.put("data", data);
		return json;
	}
	
	public int listWaiter(Map<String, Object> and,Map<String, Object> or
			,Map<String, Object> extraAnd,Map<String, Object> extraOr
			,int limit,int offset,String sortProp,String sortDire) throws IOException {
		try {
			Query q = Query.from(g,controller);
			q.skipnil(true);
			q.select(true);
			q.date(true);
			q.and(extraAnd);
			q.or(extraOr);
			q.and(and);
			q.or(or);
			q.limit(limit);
			q.offset(offset);
			q.order(sortProp, sortDire);
			if (beforeList(q)) {
				setStatement(list(q.getWorker()));
				return 1;
			} else {
				return 998;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return 0;
		}
	}
	
	public int boxListWaiter(Map<String, Object> and,Map<String, Object> or
			,int limit,int offset,String query) throws IOException {
		try {
			Query q = Query.from(g,controller);
			q.skipnil(true);
			q.select(true);
			q.date(true);
			q.and(and);			
			q.or(or);
			q.limit(limit);
			q.offset(offset);
			if (beforeBoxList(q,query)) {
				setStatement(boxList(q.getWorker()));
				return 1;
			} else {
				return 998;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return 0;
		}
	}

}
