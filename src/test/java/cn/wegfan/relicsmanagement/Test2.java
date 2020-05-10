package cn.wegfan.relicsmanagement;

import lombok.extern.slf4j.Slf4j;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Slf4j
public class Test2 {

    public static void main(String[] args) {
        try {
            int a = 1 / 0;
            throw new NotImplementedException();
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

}
