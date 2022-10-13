package Gin.panther.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class YdlBaseEntity implements Serializable {

    private final static long serialVersionUID = 1L;

    // 分页数据
    private int page;
    private int size;
    private Sort sort;


}
