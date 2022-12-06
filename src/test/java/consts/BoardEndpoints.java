package consts;

public class BoardEndpoints {
    public static final String BASE_URL = "https://api.trello.com/1/";
    public static final String GET_BOARD_URL = "boards/{id}";
    public static final String GET_ALL_MEMBER_BOARDS_URL = "members/{member}/boards";
    public static final String CREATE_BOARD_URL = "boards";
    public static final String DELETE_BOARD_URL = "boards/{id}";
    public static final String GET_BOARD_LISTS = "boards/{id}/lists";
    public static final String GET_BOARD_CARDS =  "lists/{id}/cards";
    public static final String CREATE_CARD_FOR_A_LIST =  "cards";
    public static final String GET_SINGLE_CARD =  "cards/{id}";
    public static final String UPDATE_BOARD =  "boards/{id}";
}
