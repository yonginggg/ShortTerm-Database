package kitchen.control;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import kitchen.model.BeanIngredientsCategory;
import kitchen.model.BeanIngredientsInformation;
import kitchen.model.BeanRecipeEvaluation;
import kitchen.model.BeanRecipeInformation;
import kitchen.model.BeanRecipeMaterial;
import kitchen.model.BeanRecipeStep;
import kitchen.model.BeanUser;
import kitchen.util.BaseException;
import kitchen.util.BusinessException;
import kitchen.util.HibernateUtil;

public class RecipeManager {
	private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

	public static Session getSession() {
		Session session = sessionFactory.openSession();
		return session;
	}

	public BeanRecipeInformation addRecipe(String recipe_name, BeanUser user, String recipe_description)
			throws BaseException {

		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();

		BeanRecipeInformation recipeInformation = new BeanRecipeInformation();
		try {
			recipeInformation.setRecipe_name(recipe_name);
			recipeInformation.setRecipe_collection_number(0);
			recipeInformation.setRecipe_description(recipe_description);
			recipeInformation.setRecipe_overall_rating(0);
			recipeInformation.setRecipe_views_number(0);
			recipeInformation.setUser_number(user.getUser_number());

			session.save(recipeInformation);
			transaction.commit();
		} catch (SessionException e) {
			throw new BusinessException("添加菜谱失败");
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}

		return recipeInformation;
	}

	public void deleRecipe(BeanRecipeInformation recipeInformation) throws BaseException {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			session.delete(recipeInformation);
			transaction.commit();
		} catch (SessionException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
	}

//	修改菜谱
	public BeanRecipeInformation changRecipe(BeanRecipeInformation recipe, String newDescription) {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			recipe.setRecipe_description(newDescription);
			session.update(recipe);
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
		return recipe;
	}

	public List<BeanRecipeInformation> loadAllRecipe() throws BaseException {
		List<BeanRecipeInformation> recipeInformations = new ArrayList<BeanRecipeInformation>();
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			String hql = "from BeanRecipeInformation";
			org.hibernate.query.Query query = session.createQuery(hql);
			recipeInformations = query.list();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
		return recipeInformations;
	}

	public List<BeanRecipeInformation> loadAllRecipe(BeanUser user) throws BaseException {
		List<BeanRecipeInformation> recipeInformations = new ArrayList<BeanRecipeInformation>();
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			String hql = "from BeanRecipeInformation where user_number=:num";
			org.hibernate.query.Query query = session.createQuery(hql);
			query.setInteger("num", user.getUser_number());
			recipeInformations = query.list();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
		return recipeInformations;
	}

	// 收藏夹
	public List<BeanRecipeInformation> loadAllRecipeCollection(BeanUser user) throws BaseException {
		List<BeanRecipeEvaluation> evaluations = new ArrayList<BeanRecipeEvaluation>();
		List<BeanRecipeInformation> informations = new ArrayList<BeanRecipeInformation>();
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			String hql = "from BeanRecipeEvaluation where user_number=:num and evaluation_collection_sign='收藏'";
			org.hibernate.query.Query query = session.createQuery(hql);
			query.setInteger("num", user.getUser_number());
			evaluations = query.list();

			for (int i = 0; i < evaluations.size(); i++) {
//				hql = "from BeanRecipeInformation where recipe_number=:num";
//			query = session.createQuery(hql);
//			query.setInteger("num", evaluations.get(i).getRecipe_number());
				BeanRecipeInformation j = (BeanRecipeInformation) session.get(BeanRecipeInformation.class,
						evaluations.get(i).getRecipe_number());
				informations.add(j);
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
		return informations;
	}

	public List<BeanRecipeInformation> searchRecipe(String recipe_name) throws BaseException {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();

		List<BeanRecipeInformation> recipeInformations = new ArrayList<BeanRecipeInformation>();
		try {
			String hql = "from BeanRecipeInformation r where r.recipe_name like '%" + recipe_name + "%'";
			Query query = session.createQuery(hql);
			// query.setString("recipe_name","'%"+recipe_name+"%'");
			recipeInformations = query.list();
		} catch (SessionException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			session.close();
		}

		return recipeInformations;
	}

	public BeanRecipeMaterial addRecipeMaterial(BeanRecipeInformation recipeInformation, int ingredients_number,
			double quantity, String unit) throws BaseException {

		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();

		BeanRecipeMaterial recipeMaterial = new BeanRecipeMaterial();
		try {
			recipeMaterial.setRecipe_number(recipeInformation.getRecipe_number());
			recipeMaterial.setIngredients_number(ingredients_number);
			recipeMaterial.setQuantity(quantity);
			recipeMaterial.setUnit(unit);

			session.save(recipeMaterial);
			transaction.commit();
		} catch (SessionException e) {
			throw new BusinessException("添加菜谱食材失败");
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}

		return recipeMaterial;
	}

	public void deleteMaterial(BeanRecipeMaterial material) throws BaseException {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			session.delete(material);
			transaction.commit();
		} catch (SessionException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
	}

//	修改食材
	public BeanRecipeMaterial changeMaterial(BeanRecipeMaterial material, Double quantity) {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			material.setQuantity(quantity);
			session.update(material);
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
		return material;
	}

	public List<BeanRecipeMaterial> loadAllMaterials(BeanRecipeInformation recipeInformation) throws BaseException {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();

		List<BeanRecipeMaterial> materials = new ArrayList<BeanRecipeMaterial>();
		try {
			String hql = "from BeanRecipeMaterial where recipe_number=:num";
			Query query = session.createQuery(hql);
			query.setInteger("num", recipeInformation.getRecipe_number());

			materials = query.getResultList();
			transaction.commit();

		} catch (SessionException e) {
			throw new BusinessException("查找原料失败");
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}

		return materials;
	}

	public List<BeanRecipeMaterial> loadAllMaterials(BeanIngredientsInformation information) throws BaseException {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();

		List<BeanRecipeMaterial> materials = new ArrayList<BeanRecipeMaterial>();
		try {
			String hql = "from BeanRecipeMaterial where ingredients_number=:num";
			Query query = session.createQuery(hql);
			query.setInteger("num", information.getIngredients_number());

			materials = query.getResultList();
			transaction.commit();

		} catch (SessionException e) {
			throw new BusinessException("查找原料失败");
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}

		return materials;
	}

	public BeanRecipeStep addRecipeStep(BeanRecipeInformation recipeInformation, String description)
			throws BaseException {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();

		BeanRecipeStep step = new BeanRecipeStep();
		int step_number = 1;
		try {

			// 查找某用户最大plan_order
			String hql = "select max(p.step_number) from BeanRecipeStep p where p.recipe_number=:recipe_number";
			Query query = session.createQuery(hql);
			query.setInteger("recipe_number", recipeInformation.getRecipe_number());
			if (query.uniqueResult() != null) {
				step_number = (Integer) query.uniqueResult() + 1;
			}

			step.setRecipe_number(recipeInformation.getRecipe_number());
			step.setStep_number(step_number);
			step.setStep_description(description);

			session.save(step);
			transaction.commit();
		} catch (SessionException e) {
			throw new BusinessException("添加菜谱步骤失败");
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}

		return step;

	}

	public void deleteStep(BeanRecipeStep step) throws BaseException {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			session.delete(step);

			// 更新剩余的步骤序号
			String hql = "update BeanRecipeStep p set p.step_number=p.step_number-1 where p.step_number>:step_number and p.recipe_number=:recipe_number";
//			hql="update BeanPlan p set p.planOrder=p.planOrder-1 where p.planOrder>:planorder and p.userId=:userid";
			Query query = session.createQuery(hql);
			query.setInteger("step_number", step.getStep_number());
			query.setInteger("recipe_number", step.getRecipe_number());
			query.executeUpdate();

			transaction.commit();
		} catch (SessionException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
	}

//	修改步骤
	public BeanRecipeStep changeStep(BeanRecipeStep step, String newDescription) {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			step.setStep_description(newDescription);
			session.update(step);
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
		return step;
	}

	public List<BeanRecipeStep> loadAllSteps(BeanRecipeInformation recipeInformation) throws BaseException {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();

		List<BeanRecipeStep> steps = new ArrayList<BeanRecipeStep>();
		try {
			String hql = "from BeanRecipeStep where recipe_number=:num";
			Query query = session.createQuery(hql);
			query.setInteger("num", recipeInformation.getRecipe_number());

			steps = query.getResultList();
			transaction.commit();

		} catch (SessionException e) {
			throw new BusinessException("查找步骤失败");
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}

		return steps;
	}

	public List<BeanRecipeEvaluation> loadAllEvaluations(BeanRecipeInformation recipeInformation) throws BaseException {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();

		List<BeanRecipeEvaluation> evaluations = new ArrayList<BeanRecipeEvaluation>();
		try {
			String hql = "from BeanRecipeEvaluation where recipe_number=:num";
			Query query = session.createQuery(hql);
			query.setInteger("num", recipeInformation.getRecipe_number());

			evaluations = query.getResultList();
			transaction.commit();

		} catch (SessionException e) {
			throw new BusinessException("查找评价失败");
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}

		return evaluations;
	}

	public BeanRecipeEvaluation addEvaluation(BeanRecipeInformation recipeInformation, BeanUser user, String content,
			String collection, double mark) throws BaseException {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		BeanRecipeEvaluation evaluation = new BeanRecipeEvaluation();

		try {
			String hql = "from BeanRecipeEvaluation where recipe_number=:r and user_number=:u";
			Query query = session.createQuery(hql);
			query.setInteger("r", recipeInformation.getRecipe_number());
			query.setInteger("u", user.getUser_number());
			if (query.uniqueResult() != null) {
				throw new BusinessException("不能重复评论");
			}
			evaluation.setEvaluation_browse_sign(0);
			evaluation.setEvaluation_collection_sign(collection);
			evaluation.setEvaluation_conten(content);
			evaluation.setEvaluation_grade(mark);
			evaluation.setRecipe_number(recipeInformation.getRecipe_number());
			evaluation.setUser_number(user.getUser_number());

			session.save(evaluation);
			transaction.commit();
		} catch (SessionException e) {
			throw new BusinessException("评分失败");
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}

		return evaluation;
	}

	public void changeRecipeOverallRating(BeanRecipeInformation recipe, double mark) throws BaseException {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();

		RecipeManager recipeManager = new RecipeManager();
		try {
			List<BeanRecipeEvaluation> recipeEvaluations = recipeManager.loadAllEvaluations(recipe);
			int num = recipeEvaluations.size();
			if (num == 0) {
				num = 1;
			} else {
				num++;
			}

			double allGrade = 0;
			if (recipeEvaluations != null && !recipeEvaluations.isEmpty()) {
				for (int i = 0; i < recipeEvaluations.size(); i++) {
					allGrade += recipeEvaluations.get(i).getEvaluation_grade();
				}
				allGrade += mark;
			} else {
				allGrade = mark;
			}
			recipe.setRecipe_overall_rating(allGrade / num);

			session.saveOrUpdate(recipe);
			transaction.commit();
		} catch (SessionException e) {
			throw new BusinessException("综合评分失败");
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
	}

//	有人收藏菜谱时，菜谱收藏数量+1；
	public void addRecipeCollection(BeanRecipeInformation recipeInformation) throws BaseException {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			int num = recipeInformation.getRecipe_collection_number();
			recipeInformation.setRecipe_collection_number(num + 1);

			session.update(recipeInformation);
			transaction.commit();
		} catch (SessionException e) {
			throw new BusinessException("收藏次数+1失败");
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
	}

//	查看所有菜谱时,点击菜谱, 浏览+1
	public void addRecipeView(BeanRecipeInformation recipeInformation) throws BaseException {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			int num = recipeInformation.getRecipe_views_number();
			recipeInformation.setRecipe_views_number(num + 1);

			session.update(recipeInformation);
			transaction.commit();
		} catch (SessionException e) {
			throw new BusinessException("浏览次数+1失败");
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
	}

//	查看所有菜谱时,点击评价, 浏览+1
	public void addEvaluationView(BeanRecipeEvaluation evaluation) throws BaseException {
		Session session = HibernateUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			int num = evaluation.getEvaluation_browse_sign();
			evaluation.setEvaluation_browse_sign(num + 1);

			session.update(evaluation);
			transaction.commit();
		} catch (SessionException e) {
			throw new BusinessException("浏览次数+1失败");
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
	}
}
