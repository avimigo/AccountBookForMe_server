package com.example.AccountBookForMe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.AccountBookForMe.dto.Filter;
import com.example.AccountBookForMe.dto.Name;
import com.example.AccountBookForMe.entity.Category;
import com.example.AccountBookForMe.exception.AbfmNotFoundException;
import com.example.AccountBookForMe.repository.CategoryRepository;
import com.example.AccountBookForMe.repository.ItemRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ItemRepository itemRepository;

    /**
     * リスト表示用のデータを全件取得
     * @return リスト表示用のデータ
     */
    public List<Filter> findAll() {
    	
    	List<Filter> categoryList = new ArrayList<>();
    	
    	categoryRepository.findAll().forEach(category -> {
    		categoryList.add(new Filter(category.getId(), category.getName()));
    	});

    	return categoryList;
    }
    
    /**
     * 新規作成
     * @param name : カテゴリ名
     * @return リスト表示用のデータ
     */
    public List<Filter> create(Name name) {
    	categoryRepository.save(new Category(name.getName()));
    	return findAll();
    }
    
    /**
     * 更新
     * @param filter : カテゴリID、カテゴリ名
     * @return リスト表示用のデータ
     */
    @Transactional
    public List<Filter> update(Filter filter) {
    	
		Category category = categoryRepository.findById(filter.getId())
				.orElseThrow(() -> new AbfmNotFoundException("Not found category id: " + filter.getId()));
		
		category.setName(filter.getName());
		categoryRepository.save(category);
    	return findAll();
    }
    
    /**
     * 削除
     * @param id : カテゴリID
     * @return リスト表示用のデータ
     */
    @Transactional
    public List<Filter> delete(Long id) {
    	
    	if (categoryRepository.existsById(id)) {
    		categoryRepository.deleteById(id);
    		
    		// 関連するItemも消す
    		itemRepository.deleteByCategoryId(id);

    		return findAll();

    	} else {
			throw new AbfmNotFoundException("Not found category id: " + id);
    	}
    }
}
