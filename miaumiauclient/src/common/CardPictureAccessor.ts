class CardPictureAccessor {

    private static THEME = "selfmade";

    public static getLinkByName(name: string | undefined) {
        return "/decks/" + CardPictureAccessor.THEME + "/" + name + ".png";
    }
}

export default CardPictureAccessor;