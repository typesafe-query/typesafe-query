package com.github.typesafe_query;

import java.util.Objects;

public abstract class ModelBase<T extends ModelBase<T>> {
	
	private Class<T> modelClass;
	
	private ModelHandler<T> modelHandler;
	
	public ModelBase(ModelDescription<T> description) {
		//TODO this.getClass()から色々取得することが望ましい..
		
		this.modelClass = description.getModelClass();
		
		this.modelHandler = new DefaultModelHandler<>(
			Objects.requireNonNull(description)
		);
	}
	
	/**
	 * This method is going to be deleted soon.Use create method!
	 * @return
	 */
	@Deprecated
	public Long createByGeneratedKey(){
		//TODO castが気に食わない
		//TODO generate対象だったら自動的に呼び分けてセットするほうがいい。呼び分けさせるのは気に食わない
		return modelHandler.createByGeneratedKey(modelClass.cast(this));
	}
	
	public boolean create(){
		//TODO castが気に食わない
		return modelHandler.create(modelClass.cast(this));
	}
	
	public boolean save(){
		//TODO castが気に食わない
		return modelHandler.save(modelClass.cast(this));
	}
	
	public boolean delete(){
		//TODO castが気に食わない
		return this.modelHandler.delete(modelClass.cast(this));
	}
}
