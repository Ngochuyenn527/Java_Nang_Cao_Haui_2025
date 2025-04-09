package com.example.demo.utils;

public class MapUtils {
//	public static <T> T getObject(Map<String,Object> maps, String key, Class<T> tClass) {
//		Object obj = maps.getOrDefault(key, null);
//		if(obj != null) {
//			if(tClass.getTypeName().equals("java.lang.Long")) {
//				obj = obj != "" ? Long.valueOf(obj.toString()) : null;
//			}
//			else if(tClass.getTypeName().equals("java.lang.Integer")) {
//				obj = obj != "" ? Integer.valueOf(obj.toString()) : null;
//			}
//			else if(tClass.getTypeName().equals("java.lang.String")) {
//				obj = obj.toString();
//			}
//			return tClass.cast(obj);
//		}
//		return null;
//	}

    public static <T> T getObjects(Object item, Class<T> tClass) {
        if (item != null) {
            if (tClass.getTypeName().equals("java.lang.Long")) {
                item = item != "" ? Long.valueOf(item.toString()) : null;
            } else if (tClass.getTypeName().equals("java.lang.Integer")) {
                item = item != "" ? Integer.valueOf(item.toString()) : null;
            } else if (tClass.getTypeName().equals("java.lang.String")) {
                item = item.toString();
            }
            return tClass.cast(item);
        }
        return null;
    }


//    public static <T> T getObjects(Map<String, Object> params, String key, Class<T> tClass) {
//        // Lấy giá trị từ params với khóa key.
//        // Nếu không tìm thấy key, giá trị mặc định (null) được trả về.
//        Object obj = params.getOrDefault(key, null);
//
//        if (obj != null) {
//            // tClass.getTypeName():
//            // Trả về tên đầy đủ của kiểu dữ liệu (ví dụ: "java.lang.Long")
//            if (tClass.getTypeName().equals("java.lang.Long")) {
//                // chuyển đổi giá trị obj sang Long bằng Long.valueOf(obj.toString()).
//                // gán obj = (kết quả của toán tử 3 ngôi)
//                obj = obj != "" ? Long.valueOf(obj.toString()) : null;
//            } else if (tClass.getTypeName().equals("java.lang.Integer")) {
//                obj = obj != "" ? Integer.valueOf(obj.toString()) : null;
//            } else if (tClass.getTypeName().equals("java.lang.String")) {
//                obj = obj.toString();
//            }
//
//            return tClass.cast(obj); // ép kiểu dữ liệu của obj sang kiểu T (kiểu mong muốn)
//        }
//        return null; // Nếu giá trị obj là null, phương thức sẽ trả về null.
//    }

}


