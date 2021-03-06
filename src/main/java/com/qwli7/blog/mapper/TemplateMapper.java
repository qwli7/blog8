package com.qwli7.blog.mapper;

import com.qwli7.blog.entity.Template;
import com.qwli7.blog.entity.vo.TemplateQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * 模板 Mapper
 * @author liqiwen
 * @since 1.2
 */
@Mapper
public interface TemplateMapper {

    /**
     * 插入模板
     * @param template template
     */
    void insert(Template template);

    /**
     * 根据 id 查询一个模板
     * @param id id
     * @return Template
     */
    Optional<Template> findById(int id);

    /**
     * 根据名称查询一个模板
     * @param name name
     * @return Template
     */
    Optional<Template> findByName(String name);

    /**
     * 更新模板
     * @param template template
     */
    void update(Template template);

    /**
     * 删除一个模板
     * @param id id
     */
    void deleteById(int id);

    /**
     * 查询所有的模板
     * @param queryParam queryParam
     * @return List
     */
    List<Template> findAll(TemplateQueryParam queryParam);

    /**
     * 批量插入模板列表
     * @param templates defaultTemplates
     */
    void batchInsert(List<Template> templates);

    /**
     * 根据 pattern 查找模板
     * @param pattern pattern
     * @return Template
     */
    Optional<Template> findByPattern(String pattern);
}
