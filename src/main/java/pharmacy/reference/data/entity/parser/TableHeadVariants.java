package pharmacy.reference.data.entity.parser;

public class TableHeadVariants {

    public static final String NAME = "columnMedicineNameNumber";
    public static final String MANUFACTURE = "columnMedicineManufactureNumber";
    public static final String QUANTITY = "columnMedicineQuantityNumber";
    public static final String PRICE = "columnMedicinePriceNumber";
    public static final String COUNTRY = "columnMedicineCountryNumber";

    public static String tableHeadParse(String cellValue) {

        cellValue = cellValue.trim().toLowerCase().replaceAll("\\s+", "").replaceAll("\uFEFF", "");
        switch (cellValue) {
            case "наименование":
            case "товар":
            case "preparat":
            case "наименованиетовара":
            case "названиетовара":
            case "название":
            case "номенклатура":
                return NAME;
            case "производитель":
            case "изготовитель":
            case "фирма-производитель":
            case "proizv":
                return MANUFACTURE;
            case "количество":
            case "кол-во":
            case "кол-во(ед)":
            case "количествотовара":
            case "кол.":
            case "ost":
            case "остатокнаскладе":
            case "остаток":
                return QUANTITY;
            case "цена":
            case "цена,руб.":
            case "средниеценыценарозн":
            case "средняяцена":
            case "ценарозн.":
            case "розничнаяцена":
            case "cena":
            case "ценапопрайсу({currencysymbol})заединицутовара":
                return PRICE;
            case "country":
            case "страна":
            case "странапроизводитель":
            case "strana":
                return COUNTRY;
            default:
                return null;
        }
    }

}
