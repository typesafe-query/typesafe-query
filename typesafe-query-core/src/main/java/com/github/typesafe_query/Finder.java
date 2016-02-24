/**
 * 
 */
package com.github.typesafe_query;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.Order;

/**
 * モデルの検索を行います。
 * <p>検索を行うクラスです</p>
 * 
 * @author Takahiko Sato(MOSA architect Inc.)
 */
public interface Finder<I,T> {
	
	Finder<I,T> includeDefault();
	
	T requiredById(I id);
	
	/**
	 * プライマリーキーで検索します
	 * @param id プライマリーキー
	 * @return Model
	 */
	Optional<T> byId(I id);
	
	/**
	 * 件数を返します。
	 * @return 件数
	 */
	long count();

	/**
	 * 条件を指定して件数を返します。
	 * @param expressions 検索条件
	 * @return 件数
	 */
	long countWhere(Exp... expressions);

	/**
	 * 一覧を返します
	 * @param orders ソート順
	 * @return 一覧
	 */
	List<T> list(Order...orders);
	
	/**
	 * 件数を指定して一覧を返します。
	 * @param limit 最大件数
	 * @param orders ソート順
	 * @return 一覧
	 */
	List<T> list(int limit,Order...orders);
	
	/**
	 * 開始位置と件数を指定して一覧を返します。
	 * @param offset 開始位置
	 * @param limit 最大件数
	 * @param orders ソート順
	 * @return 一覧
	 */
	List<T> list(int offset,int limit,Order...orders);
	
	T requiredWhere(Exp... expressions);
	
	/**
	 * 条件を指定して1件取得します
	 * @param expressions 条件
	 * @return 1件の結果
	 */
	Optional<T> where(Exp... expressions);
	
	/**
	 * 条件を指定して一覧を返します。
	 * @param expression 条件
	 * @param orders ソート順
	 * @return 一覧
	 */
	List<T> listWhere(Exp expression,Order...orders);
	
	/**
	 * 条件、件数を指定して一覧を返します。
	 * @param expression 条件
	 * @param limit 最大件数
	 * @param orders ソート順
	 * @return 一覧
	 */
	List<T> listWhere(Exp expression,Integer limit,Order...orders);
	
	/**
	 * 条件、開始位置、件数を指定して一覧を返します。
	 * @param expression 条件
	 * @param offset 開始位置
	 * @param limit 件数
	 * @param orders ソート順
	 * @return 一覧
	 */
	List<T> listWhere(Exp expression,Integer offset,Integer limit,Order...orders);
	
	void fetch(Predicate<T> p, Order...orders);
	void fetch(Predicate<T> p,int limit,Order...orders);
	void fetch(Predicate<T> p,int offset,int limit,Order...orders);
	void fetch(Consumer<T> p,Order...orders);
	void fetch(Consumer<T> p,int limit,Order...orders);
	void fetch(Consumer<T> p,int offset,int limit,Order...orders);
	void fetchWhere(Exp expression,Predicate<T> p,Order...orders);
	void fetchWhere(Exp expression,Predicate<T> p,Integer limit,Order...orders);
	void fetchWhere(Exp expression,Predicate<T> p,Integer offset,Integer limit,Order...orders);
	void fetchWhere(Exp expression,Consumer<T> p,Order...orders);
	void fetchWhere(Exp expression,Consumer<T> p,Integer limit,Order...orders);
	void fetchWhere(Exp expression,Consumer<T> p,Integer offset,Integer limit,Order...orders);
}
