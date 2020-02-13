package com.zfx.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfx.gmall.pms.entity.Product;
import com.zfx.gmall.pms.mapper.*;
import com.zfx.gmall.pms.service.ProductService;
import com.zfx.gmall.vo.PageInfoVo;
import com.zfx.gmall.vo.product.PmsProductParam;
import com.zfx.gmall.vo.product.PmsProductQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
@Slf4j
@Component
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    ProductAttributeValueMapper productAttributeValueMapper;
    @Autowired
    ProductFullReductionMapper productFullReductionMapper;
    @Autowired
    ProductLadderMapper productLadderMapper;
    @Autowired
    SkuStockMapper skuStockMapper;


    //spring的所有组件全是单例，一定会出现线程安全问题
    //只要没有共享属性，一个要读，一个要改，就不会出现安全问题；
    //当前线程共享同样的数据 把下面需要使用的productId 存入
    private static final ThreadLocal<Long> threadLocal =  new ThreadLocal();
    //ThreadLocal底层原理
    private static final Map<Thread,Long> map = new HashMap();

    @Override
    public PageInfoVo productPageInfo(PmsProductQueryParam param) {

        Page<Product> page = new Page<>(param.getPageNum(),param.getPageSize());//从第*页开始，每页显示*条
        QueryWrapper<Product> wrapper = new QueryWrapper<Product>();

        if(param.getBrandId()!=null){
            wrapper.eq("brand_id",param.getBrandId());
        }
        if(param.getProductCategoryId()!=null){
            wrapper.eq("product_category_id",param.getProductCategoryId());
        }
        if(!StringUtils.isEmpty(param.getProductSn())){
            wrapper.like("product_sn",param.getProductSn());
        }

        if (!StringUtils.isEmpty(param.getKeyword())){
            wrapper.like("name",param.getKeyword());
        }
        if(param.getVerifyStatus()!=null){
            wrapper.eq("verify_status",param.getVerifyStatus());
        }
        if(param.getPublishStatus()!=null){
            wrapper.eq("publish_status",param.getPublishStatus());
        }

        IPage<Product> productIPage = productMapper.selectPage(page, wrapper);

        //前端需要的PageInfoVo格式的数据
        PageInfoVo vo = PageInfoVo.getVo(productIPage, param.getPageSize());
        return vo;
    }

    /**
     * 事务的传播行为：
     * Propagation {
     *     【REQUIRED】--此方法需要事务，如果没有就开新事务，如果之前已存在就用以前事务
     *     SUPPORTS--支持：有事务用事务，没有不用
     *     MANDATORY--强制要求： 必须在事务中运行，没有就报错
     *     【REQUIRES_NEW】--需要新的：这个方法必须用一个新的事务来做，不用混用
     *     NOT_SUPPORTED--不支持：此方法不能在事务中运行，如果有事务，暂停之前的事务；
     *     NEVER--从不用事务，否则抛异常
     *     NESTED--内嵌事务；还原点
     *
     *
     *  REQUIRED【和大方法用一个事务】
     *  REQUIRES_NEW【用一个新事务】
     *  异常机制还是异常机制
     * #####################################
     * Required_ new
     * 	外事务{
     * 	A () ; Required; A
     * 	B () ;Requires_new B
     * 	try{
     * 		C();Required; C
     *        }catch(Exception e){
     * 		//c出异常?
     *    }
     * 	D();Requires_new; D
     *
     * 	//给数据库存--外
     * 	// int i = 10/0;
     *
     * 场景1:
     * 	A方法出现了异常;由于异常机制导致代码停止，下面无法执行，数据库什么都没有
     * 场景2:
     * 	c方法出现异常: A回滚，B成功，C回滚，无法执行，外无法执行
     * 场景3:
     * 	外成了后，int i = 10/0; B, D成功。A,C,外都执行了但是必须回滚
     * 场景4:
     * 	D炸;抛异常。外事务感知到异常。A, c回滚，外执行不到，D自己回滚，成功
     * 场景5:
     * 	c用try-catch执行:出了异常回滚，由于异常被捕获，外事务投有感知异常。A,B,D都成功，C自己回滚
     *
     * 总结:
     * 	--事务传播行为过程中，只要Requires_ new被执行过就一定成功。不管后面出不出问题。异常机制还是一样的，出现异常代码以后不执行。
     * 	--Required只要感觉到异常就一定回滚。 和外事务是什么传播行为无关
     * 	--传播行为总是来定义，当-个事务存在的时候，他内部的事务该怎么执行。
     * 	#########################################
     *
     *
     *
     * @Transactional  一定不要标准在Controller
     * //AOP做的事务
     * //基于反射调用了
     *  对象.方法（）才可以调用事务
     *  本类调用自己的方法，相当于用的同一个事务saveProduct
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveProduct(PmsProductParam productParam) {
        //1)、pms_product: 保存商品基本信息-->>【REQUIRED】--此方法需要事务，如果没有就开新事务，如果之前已存在就用以前事务
        saveBaseInfo(productParam);

        //5)、pms_sku_stock_sku_ 库存表-->>【REQUIRED】--此方法需要事务，如果没有就开新事务，如果之前已存在就用以前事务
        saveSkuStock(productParam);

        /*以下都可以try- cotch互不影响*/
        //2)、pms_product_attribute_value: 保存这个商品对应的所有属性的值-->>REQUIRED_NEW 新开事务，各自互不影响
        saveProductAttributeValue(productParam);

        //3)、pms_product_full_reduction: 保存商品的满诚信息-->>REQUIRED_NEW 新开事务，各自互不影响
        saveProductFullReduction(productParam);

        //4)、pms_product_ladder: 满减表-->>REQUIRED_NEW 新开事务，各自互不影响
        saveProductLadder(productParam);



        /*
        * 使用ThreadLocal时遵守以下两个小原则 防止内存溢出:
            ①ThreadLocal申明为private static final。
                 Private与final 尽可能不让他人修改变更引用，
                 Static 表示为类属性，只有在程序结束才会被回收。
            ②ThreadLocal使用后务必调用remove方法。
                最简单有效的方法是使用后将其移除。
        * */
        threadLocal.remove();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSkuStock(PmsProductParam productParam) {
        productParam.getSkuStockList().forEach(skuStock -> {
            skuStock.setProductId(threadLocal.get());
            //生成sku_code的默认值
            if(StringUtils.isEmpty(skuStock.getSkuCode())){
                skuStock.setSkuCode(getTimeStr()+"_"+threadLocal.get());
            }
            skuStockMapper.insert(skuStock);
        });
        log.debug("saveSkuStock-->>当前线程--{}-->线程名：{}--->productID:{}-->{}"
                ,Thread.currentThread().getId(),Thread.currentThread().getName(),threadLocal.get(),map.get(Thread.currentThread()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductLadder(PmsProductParam productParam) {
        productParam.getProductLadderList().forEach(productLadder -> {
            productLadder.setProductId(threadLocal.get());
            productLadderMapper.insert(productLadder);
        });
        log.debug("saveSkuStock-->>当前线程--{}-->线程名：{}--->productID:{}-->{}"
                ,Thread.currentThread().getId(),Thread.currentThread().getName(),threadLocal.get(),map.get(Thread.currentThread()));
        int i =1/0;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductFullReduction(PmsProductParam productParam) {
        productParam.getProductFullReductionList().forEach(productFullReduction -> {
            productFullReduction.setProductId(threadLocal.get());
            productFullReductionMapper.insert(productFullReduction);
        });
        log.debug("saveSkuStock-->>当前线程--{}-->线程名：{}--->productID:{}-->{}"
                ,Thread.currentThread().getId(),Thread.currentThread().getName(),threadLocal.get(),map.get(Thread.currentThread()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductAttributeValue(PmsProductParam productParam) {
        productParam.getProductAttributeValueList().forEach(productAttributeValue -> {
            //在pms_product_attribute_value表内需要设置其关联的productId
            productAttributeValue.setProductId(threadLocal.get());
            productAttributeValueMapper.insert(productAttributeValue);
        });
        log.debug("saveSkuStock-->>当前线程--{}-->线程名：{}--->productID:{}-->{}"
                ,Thread.currentThread().getId(),Thread.currentThread().getName(),threadLocal.get(),map.get(Thread.currentThread()));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveBaseInfo(PmsProductParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam,product);//将前台传递过来的数据源内的相同属性copy过来赋值给product
        productMapper.insert(product);
        //将商品基础信息的productId存入ThreadLocal中
        threadLocal.set(product.getId());
        //ThreadLocal底层原理实现
        map.put(Thread.currentThread(),product.getId());
    }

    public String getTimeStr(){
        /*
        20191531191523234  //一般用于生成订单流水号
        2019-08-31 19:15:23:234
        2019-08-31
        2019-08-31
        2019-08-31 19:15:23
         */

        LocalDateTime now=LocalDateTime.now();

        //年月日时分秒毫秒
//        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        //年月日时分秒毫秒
//        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")));
        //生成年月日
//        System.out.println(now.format(DateTimeFormatter.ISO_DATE));
//        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        //年月日时分秒
//        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
