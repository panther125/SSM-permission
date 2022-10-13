package Gin.SSM;


import Gin.panther.entity.YdlRole;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Streamtest {

    @Test
    public void test01(){
        YdlRole ydlRole1 = new YdlRole();
        ydlRole1.setRoleId(1L);

        YdlRole ydlRole2 = new YdlRole();
        ydlRole1.setRoleId(2L);

        List<YdlRole> ydlRole = List.of(ydlRole1,ydlRole2);

        Stream<Long> longStream = ydlRole.stream().map(YdlRole::getRoleId);
        longStream.forEach(System.out::println);

        List<Long> collect = ydlRole.stream().map(YdlRole::getRoleId).collect(Collectors.toList());
        collect.forEach(System.out::println);
    }
}
