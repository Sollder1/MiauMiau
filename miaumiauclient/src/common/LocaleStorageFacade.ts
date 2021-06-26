class LocaleStorageFacade {

    public static getUserId() {
        return localStorage.getItem("user-id");
    }

}

export default LocaleStorageFacade;