package com.example.demo.views;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class CommonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public static HttpHeaders createAuthHeaders() {
        String auth = "nguyenvana:123456";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public static void handleApiError(HttpStatusCode status, String responseBody) {
        String message;
        int code = status.value();

        if (code == 401) {
            message = "Yêu cầu xác thực - Sai tên người dùng hoặc mật khẩu!";
        } else if (code == 500) {
            message = "Lỗi máy chủ: API gặp sự cố.";
        } else {
            message = "Lỗi khi gọi API: " + code;
        }
        showError(message);
    }

    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static Long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá trị không hợp lệ");
        }
    }

    public static Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá trị không hợp lệ");
        }
    }

    public static Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá trị không hợp lệ");
        }
    }

    public static void clearAllTextFieldsIn(Pane rootPane) {
        for (javafx.scene.Node node : rootPane.getChildrenUnmodifiable()) {

            if (node instanceof TextField) {
                ((TextField) node).clear();

            } else if (node instanceof DatePicker) {
                ((DatePicker) node).setValue(null);

            } else if (node instanceof ComboBox) {
                ((ComboBox<?>) node).setValue(null);

            } else if (node instanceof Pane) {
                // Đệ quy nếu bên trong có container lồng
                clearAllTextFieldsIn((Pane) node);
            }
        }
    }

    public static void hideEditPanes(Pane... editPanes) {
        for (Pane pane : editPanes) {
            pane.setVisible(false);
            pane.setManaged(false);
        }
    }

    public static void showOnly(Pane visiblePane, Pane... allPanes) {
        List<Pane> panes = Arrays.asList(allPanes);
        for (Pane pane : panes) {
            boolean isVisible = pane == visiblePane;
            pane.setVisible(isVisible);
            pane.setManaged(isVisible);
        }
    }
}
