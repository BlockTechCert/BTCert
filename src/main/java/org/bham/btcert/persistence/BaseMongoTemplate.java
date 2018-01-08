package org.bham.btcert.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bham.btcert.model.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.mongodb.WriteResult;

/**
 * 
 * @Title: BaseMongoTemplate.java
 * @Package org.bham.btcert.persistence
 * @Description: TODO Mongo data handler
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */
@Service
public class BaseMongoTemplate {

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * get list by pages
	 * 
	 * @param <T>
	 * @param <T>
	 * @param u_name
	 * @return
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	public <T> PageModel<T> getListPage(PageModel<T> page, Class<T> entityClass) {

		// Query query=new BasicQuery(page.getQueryObject());

		// 部分数据权限以后集成
		/*
		 * UserDetails userDetails = (UserDetails)
		 * SecurityContextHolder.getContext() .getAuthentication()
		 * .getPrincipal(); //String currentUserName =
		 * userDetails.getUsername(); //获取当前用户 Collection<GrantedAuthority>
		 * authorities = (Collection<GrantedAuthority>)
		 * userDetails.getAuthorities(); for (GrantedAuthority grantedAuthority
		 * : authorities) { Criteria criterianew = new Criteria();
		 * criterianew.where("data_role").is(grantedAuthority.getAuthority());
		 * criteria.orOperator(criterianew); }
		 */

		// 提供两种方式

		Map<String, String> QueryObjectMap = JSON.parseObject(page.getQueryObject(), Map.class);
		
		
		Criteria criteria = new Criteria();

		for (String key : QueryObjectMap.keySet()) {
			
			if ("!null".equalsIgnoreCase(QueryObjectMap.get(key))) {
				criteria.and(key).ne("");
				//continue;
			} else if (QueryObjectMap.get(key).contains("regex_")) {
				String[] value = QueryObjectMap.get(key).split("_");
				criteria.and(key).regex(value[1]);
				//continue;
			} else if (QueryObjectMap.get(key).contains("notExists_")) {
				String[] value = QueryObjectMap.get(key).split("_");
				criteria.and(key).not().regex(value[1]);
				//continue;
			}else if (QueryObjectMap.get(key).contains("gtOperator_")) {
				String[] value = QueryObjectMap.get(key).split("_");
				criteria.orOperator(new Criteria().where(key).gt(value[1]));
				//continue;
			}else if (QueryObjectMap.get(key).contains("matchAll_")) {
				String[] value = QueryObjectMap.get(key).split("_");
				System.err.println(QueryObjectMap.get(key));
				//QueryObjectMap.remove("id");
				
				if(value!=null && value.length > 1){
					String va = value[1];
					if(!va.equals("undefined") && !va.equals("null") && !va.equals("")){
						Field [] fields = entityClass.getDeclaredFields(); 
						List<Criteria> list = new ArrayList<Criteria>();
						for(Field f:fields){  
							list.add(new Criteria().where(f.getName()).regex(".*"+va+".*"));
				        } 
						criteria.orOperator((Criteria[]) list.toArray(new Criteria[fields.length]));
						//continue;
					}					
				}
			} else{
				criteria.and(key).is(QueryObjectMap.get(key));
			}
		}

		Query query = new Query(criteria);
		

		// 查询总数
		int rowCount = (int) mongoTemplate.count(query, entityClass);
		page.setTotal(rowCount);

		int skip = (page.getPageNumber() - 1) * page.getPageSize(); // 跳過
		page.setSkip(skip);

		String sortType = page.getSortType();
		if (sortType != null && !"".equals(sortType)) {
			if ("ASC".equalsIgnoreCase(sortType)) {
				query.with(new Sort(Direction.ASC, page.getSortColumn()));
			}
			if ("DESC".equalsIgnoreCase(sortType)) {
				query.with(new Sort(Direction.DESC, page.getSortColumn()));
			}
		}
		
		System.out.println(query);

		query.skip(page.getSkip()).limit(page.getPageSize());

		List<T> datas = mongoTemplate.find(query, entityClass);
		page.setRows(datas);

		// 此处可以用游标进行查询优化，留作项目二期
		/*
		 * List<Orders>datas=newArrayList<Orders>(); while (dbCursor.hasNext())
		 * { datas.add(morphia.fromDBObject(Orders.class, dbCursor.next())); }
		 */
		return page;
	}

	/**
	 * 
	 * delete
	 * @param <T>
	 * @param <T>
	 * @param u_name
	 * @return
	 */
	public <T> int delEntity(String delIds, Class<T> entityClass) {
		try {
			String queryObject = "{_id:'" + delIds + "'}";
			Query query = new BasicQuery(queryObject);
			mongoTemplate.remove(query, entityClass);
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	/**
	 * Batch
	 * @param list
	 * @return
	 */
	public <T> int saveEntityBatch(List<T> list) {
		try {
			mongoTemplate.insertAll(list);
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	/**
	 * save Entity
	 * @param entity
	 * @return
	 */
	public <T> int saveEntity(T entity) {
		try {
			mongoTemplate.save(entity);
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	
	/**
	 * find all
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> findAll(Class<T> entityClass) {
		List<T> list = mongoTemplate.findAll(entityClass);
		return list;
	}

	/**
	 * get Entity by queryColumn
	 * @param queryColumn
	 * @param id
	 * @param entityClass
	 * @return
	 */
	public <T> T getEntity(String queryColumn, String id, Class<T> entityClass) {
		Query query = new Query(Criteria.where(queryColumn).is(id));
		List<T> list = mongoTemplate.find(query, entityClass);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * get Entity List
	 * @param queryColumn
	 * @param id
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> getEntityList(String queryColumn, String id, Class<T> entityClass) {
		Query query = new Query(Criteria.where(queryColumn).is(id));
		List<T> list = mongoTemplate.find(query, entityClass);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list;
	}
	
	/**
	 * get Entity by ids
	 * @param delIds
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> getEntity(String delIds, Class<T> entityClass) {
		String queryObject = "{_id:{$in:[" + delIds + "]}}";
		Query query = new BasicQuery(queryObject);
		List<T> list = mongoTemplate.find(query, entityClass);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list;
	}

	/**
	 * update Entity
	 * @param id
	 * @param entity
	 * @param entityClass
	 * @return
	 */
	public <T> int updateEntity(String id, T entity, Class<T> entityClass) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update();

		Field[] fields = entity.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			update.set(fields[i].getName(), getFieldValueByName(fields[i].getName(), entity));
			// fields[i].getType()
		}

		WriteResult result = mongoTemplate.updateFirst(query, update, entityClass);
		if (result != null)
			return result.getN();
		else
			return 0;
	}

	/**
	 * get Field value by name
	 * @param fieldName
	 * @param o
	 * @return
	 */
	private static Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}

}
