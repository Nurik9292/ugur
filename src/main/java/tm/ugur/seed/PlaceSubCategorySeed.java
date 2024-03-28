package tm.ugur.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tm.ugur.models.PlaceCategory;
import tm.ugur.models.PlaceCategoryTranslation;
import tm.ugur.models.PlaceSubCategory;
import tm.ugur.models.PlaceSubCategoryTranslation;
import tm.ugur.services.admin.PlaceCategoryTranslationService;
import tm.ugur.services.admin.PlaceSubCategoryService;
import tm.ugur.services.admin.PlaceSubCategoryTranslationService;

import java.util.*;

@Component
@Order(11)
public class PlaceSubCategorySeed implements CommandLineRunner {

    private final PlaceSubCategoryService placeSubCategoryService;
    private final PlaceCategoryTranslationService categoryTranslationService;
    private final PlaceSubCategoryTranslationService subCategoryTranslationService;

    @Autowired
    public PlaceSubCategorySeed(PlaceSubCategoryService placeSubCategoryService,
                                PlaceCategoryTranslationService categoryTranslationService,
                                PlaceSubCategoryTranslationService subCategoryTranslationService) {
        this.placeSubCategoryService = placeSubCategoryService;
        this.categoryTranslationService = categoryTranslationService;
        this.subCategoryTranslationService = subCategoryTranslationService;
    }

    @Override
    public void run(String... args) throws Exception {
       if(placeSubCategoryService.findAll().isEmpty()){
           Map<String, List<PlaceSubCategoryTranslation>> categories = new HashMap<>();
           categories.put("cafe", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Kofe"),
                   new PlaceSubCategoryTranslation("ru", "Кофе"),
                   new PlaceSubCategoryTranslation("en", "Coffee")
           ));
           categories.put("nationalDishes", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Milli tagamlar"),
                   new PlaceSubCategoryTranslation("ru", "Национальные блюда"),
                   new PlaceSubCategoryTranslation("en", "National dishes")
           ));
           categories.put("turkishCuisine", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Türk aşhanasy"),
                   new PlaceSubCategoryTranslation("ru", "Турецкая кухня"),
                   new PlaceSubCategoryTranslation("en", "Turkish cuisine")
           ));
           categories.put("asianCuisine", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Aziýa aşhanasy"),
                   new PlaceSubCategoryTranslation("ru", "Азиатская кухня"),
                   new PlaceSubCategoryTranslation("en", "Asian cuisine")
           ));
           categories.put("europeanCuisine", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Europeanewropa aşhanasy"),
                   new PlaceSubCategoryTranslation("ru", "Европейская кухня"),
                   new PlaceSubCategoryTranslation("en", "European cuisine")
           ));
           categories.put("sushi", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Suşi"),
                   new PlaceSubCategoryTranslation("ru", "Суши"),
                   new PlaceSubCategoryTranslation("en", "Sushi")
           ));
           categories.put("steak", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Biftek"),
                   new PlaceSubCategoryTranslation("ru", "Стейк"),
                   new PlaceSubCategoryTranslation("en", "Steak")
           ));
           categories.put("iceCream", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Doňdurma"),
                   new PlaceSubCategoryTranslation("ru", "Мороженое"),
                   new PlaceSubCategoryTranslation("en", "Ice cream")
           ));
           categories.put("pizza", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Pitsa"),
                   new PlaceSubCategoryTranslation("ru", "Пицца"),
                   new PlaceSubCategoryTranslation("en", "Pizza")
           ));
           categories.put("burger", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Burger"),
                   new PlaceSubCategoryTranslation("ru", "Бургер"),
                   new PlaceSubCategoryTranslation("en", "Burger")
           ));
           categories.put("brazier", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Mangal"),
                   new PlaceSubCategoryTranslation("ru", "Мангал"),
                   new PlaceSubCategoryTranslation("en", "Brazier")
           ));
           categories.put("breakfast", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Ertirlik"),
                   new PlaceSubCategoryTranslation("ru", "Завтраки"),
                   new PlaceSubCategoryTranslation("en", "Breakfast")
           ));
           categories.put("fish", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Balyk"),
                   new PlaceSubCategoryTranslation("ru", "Рыба"),
                   new PlaceSubCategoryTranslation("en", "Fish")
           ));
           categories.put("sweets", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Süýjüler"),
                   new PlaceSubCategoryTranslation("ru", "Сладости"),
                   new PlaceSubCategoryTranslation("en", "Sweets")
           ));
           categories.put("delivery", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Eltip bermek"),
                   new PlaceSubCategoryTranslation("ru", "Доставка"),
                   new PlaceSubCategoryTranslation("en", "Delivery")
           ));
           categories.put("doner", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Döner"),
                   new PlaceSubCategoryTranslation("ru", "Дёнер"),
                   new PlaceSubCategoryTranslation("en", "Doner")
           ));

           Optional<PlaceCategoryTranslation> categoryTranslation =
                   categoryTranslationService.findByLocaleAndTitle("ru", "Кафе");

           categoryTranslation.ifPresent(translation -> {
               PlaceCategory placeCategory = translation.getPlaceCategory();
               categories.forEach((key, value) -> {
                   PlaceSubCategory placeSubCategory = placeSubCategoryService.store(new PlaceSubCategory(placeCategory, value));
                   value.forEach(c -> {
                       c.setPlaceSubCategory(placeSubCategory);
                       subCategoryTranslationService.store(c);
                   });
               });
           });

           Map<String, List<PlaceSubCategoryTranslation>> travelCategories = new HashMap<>();
           travelCategories.put("tourist agency", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Syýahatçylyk gullugy"),
                   new PlaceSubCategoryTranslation("ru", "Туристическое агентство"),
                   new PlaceSubCategoryTranslation("en", "Tourist agency")
           ));
           travelCategories.put("hotel", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Myhmanhana"),
                   new PlaceSubCategoryTranslation("ru", "Отель"),
                   new PlaceSubCategoryTranslation("en", "Hotel")
           ));
           travelCategories.put("avaza", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Awaza"),
                   new PlaceSubCategoryTranslation("ru", "Аваза"),
                   new PlaceSubCategoryTranslation("en", "Avaza")
           ));
           travelCategories.put("airport", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Aeroport"),
                   new PlaceSubCategoryTranslation("ru", "Аэропорт"),
                   new PlaceSubCategoryTranslation("en", "Airport")
           ));
           travelCategories.put("railway", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Demir ýol"),
                   new PlaceSubCategoryTranslation("ru", "Вокзал"),
                   new PlaceSubCategoryTranslation("en", "Railway station")
           ));
           travelCategories.put("monuments", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Taryhy ýadygärlikler"),
                   new PlaceSubCategoryTranslation("ru", "Исторические памятники"),
                   new PlaceSubCategoryTranslation("en", "Historical monuments")
           ));


           Optional<PlaceCategoryTranslation> travelCategoryTranslation =
                   categoryTranslationService.findByLocaleAndTitle("ru", "Путешествие");

           travelCategoryTranslation.ifPresent(translation -> {
               PlaceCategory placeCategory = translation.getPlaceCategory();
               travelCategories.forEach((key, value) -> {
                   PlaceSubCategory placeSubCategory = placeSubCategoryService.store(new PlaceSubCategory(placeCategory, value));
                   value.forEach(c -> {
                       c.setPlaceSubCategory(placeSubCategory);
                       subCategoryTranslationService.store(c);
                   });
               });
           });

           Map<String, List<PlaceSubCategoryTranslation>> healthCategories = new HashMap<>();
           healthCategories.put("clinics", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Klinikalar"),
                   new PlaceSubCategoryTranslation("ru", "Поликлиники"),
                   new PlaceSubCategoryTranslation("en", "Clinics")
           ));
           healthCategories.put("optics", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Optika"),
                   new PlaceSubCategoryTranslation("ru", "Оптика"),
                   new PlaceSubCategoryTranslation("en", "Optics")
           ));
           healthCategories.put("sanatoriums", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Sanatoriýalar"),
                   new PlaceSubCategoryTranslation("ru", "Санатории"),
                   new PlaceSubCategoryTranslation("en", "Sanatoriums")
           ));
           healthCategories.put("pharmacies", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Dermanhanalar"),
                   new PlaceSubCategoryTranslation("ru", "Аптеки"),
                   new PlaceSubCategoryTranslation("en", "Pharmacies")
           ));
           healthCategories.put("medical", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Lukmançylyk enjamlary"),
                   new PlaceSubCategoryTranslation("ru", "Медтехника"),
                   new PlaceSubCategoryTranslation("en", "Medical equipment")
           ));

           Optional<PlaceCategoryTranslation> healthCategoryTranslation =
                   categoryTranslationService.findByLocaleAndTitle("ru", "Здоровье");

           healthCategoryTranslation.ifPresent(translation -> {
               PlaceCategory placeCategory = translation.getPlaceCategory();
               healthCategories.forEach((key, value) -> {
                   PlaceSubCategory placeSubCategory = placeSubCategoryService.store(new PlaceSubCategory(placeCategory, value));
                   value.forEach(c -> {
                       c.setPlaceSubCategory(placeSubCategory);
                       subCategoryTranslationService.store(c);
                   });
               });
           });

           Map<String, List<PlaceSubCategoryTranslation>> serviceCategories = new HashMap<>();
           serviceCategories.put("translation", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Terjime merkezi"),
                   new PlaceSubCategoryTranslation("ru", "Центр переводов"),
                   new PlaceSubCategoryTranslation("en", "Translation Center")
           ));
           serviceCategories.put("recruitment", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Işe kabul ediş gullugy"),
                   new PlaceSubCategoryTranslation("ru", "Кадровое агентство"),
                   new PlaceSubCategoryTranslation("en", "Recruitment agency")
           ));
           serviceCategories.put("cleaning", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Gury arassalamak"),
                   new PlaceSubCategoryTranslation("ru", "Химчистка"),
                   new PlaceSubCategoryTranslation("en", "Dry cleaning")
           ));
           serviceCategories.put("real estate", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Gozgalmaýan emläk gullugy"),
                   new PlaceSubCategoryTranslation("ru", "Агентство недвижимости"),
                   new PlaceSubCategoryTranslation("en", "Real estate agency")
           ));
           serviceCategories.put("atelier", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Atelýe"),
                   new PlaceSubCategoryTranslation("ru", "Ателье"),
                   new PlaceSubCategoryTranslation("en", "Atelier")
           ));
           serviceCategories.put("it", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Dizaýn we programmirleme"),
                   new PlaceSubCategoryTranslation("ru", "Дизайн и Программирование"),
                   new PlaceSubCategoryTranslation("en", "Design and Programming")
           ));
           serviceCategories.put("advertising", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Mahabat we çap"),
                   new PlaceSubCategoryTranslation("ru", "Реклама и полиграфия"),
                   new PlaceSubCategoryTranslation("en", "Advertising and printing")
           ));
           serviceCategories.put("beauty", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Gözellik salonlary"),
                   new PlaceSubCategoryTranslation("ru", "Салоны красоты"),
                   new PlaceSubCategoryTranslation("en", "Beauty Salons")
           ));
           serviceCategories.put("logistics", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Logistika we buhgalteriýa"),
                   new PlaceSubCategoryTranslation("ru", "Логистика и Бухгалтерия"),
                   new PlaceSubCategoryTranslation("en", "Logistics and Accounting")
           ));
           serviceCategories.put("car service", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Awtoulag hyzmaty"),
                   new PlaceSubCategoryTranslation("ru", "Автосервис"),
                   new PlaceSubCategoryTranslation("en", "Car service")
           ));
           serviceCategories.put("workshop", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Ussahana"),
                   new PlaceSubCategoryTranslation("ru", "Мастерская"),
                   new PlaceSubCategoryTranslation("en", "Workshop")
           ));

           Optional<PlaceCategoryTranslation> serviceCategoryTranslation =
                   categoryTranslationService.findByLocaleAndTitle("ru", "Услуги");

           serviceCategoryTranslation.ifPresent(translation -> {
               PlaceCategory placeCategory = translation.getPlaceCategory();
               serviceCategories.forEach((key, value) -> {
                   PlaceSubCategory placeSubCategory = placeSubCategoryService.store(new PlaceSubCategory(placeCategory, value));
                   value.forEach(c -> {
                       c.setPlaceSubCategory(placeSubCategory);
                       subCategoryTranslationService.store(c);
                   });
               });
           });

           Map<String, List<PlaceSubCategoryTranslation>> eventCategories = new HashMap<>();
           eventCategories.put("clothes", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Banket zallaryny kärendesine almak üçin eşikler"),
                   new PlaceSubCategoryTranslation("ru", "Одежда на прокат Банкетные залы"),
                   new PlaceSubCategoryTranslation("en", "Clothes for rent Banquet halls")
           ));
           eventCategories.put("tents", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Çadyrlar"),
                   new PlaceSubCategoryTranslation("ru", "Палатки"),
                   new PlaceSubCategoryTranslation("en", "Tents")
           ));
           eventCategories.put("presenters", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Tanyşdyryjylar"),
                   new PlaceSubCategoryTranslation("ru", "Ведущие"),
                   new PlaceSubCategoryTranslation("en", "Presenters")
           ));
           eventCategories.put("dancers", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Tansçylar"),
                   new PlaceSubCategoryTranslation("ru", "Танцоры"),
                   new PlaceSubCategoryTranslation("en", "Dancers")
           ));
           eventCategories.put("wedding", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Toý salonlary"),
                   new PlaceSubCategoryTranslation("ru", "Свадебные салоны"),
                   new PlaceSubCategoryTranslation("en", "Wedding salons")
           ));
           eventCategories.put("music", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Saz"),
                   new PlaceSubCategoryTranslation("ru", "Музыка"),
                   new PlaceSubCategoryTranslation("en", "Music")
           ));
           eventCategories.put("photo", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Surat we wideo studiýalary"),
                   new PlaceSubCategoryTranslation("ru", "Фото и Видеостудии"),
                   new PlaceSubCategoryTranslation("en", "Photo and Video Studios")
           ));
           eventCategories.put("decor", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Bezeg"),
                   new PlaceSubCategoryTranslation("ru", "Оформление"),
                   new PlaceSubCategoryTranslation("en", "Decor")
           ));
           eventCategories.put("animators", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Animatorlar"),
                   new PlaceSubCategoryTranslation("ru", "Аниматоры"),
                   new PlaceSubCategoryTranslation("en", "Animators")
           ));
           eventCategories.put("cakes", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Tortlar"),
                   new PlaceSubCategoryTranslation("ru", "Торты"),
                   new PlaceSubCategoryTranslation("en", "Cakes")
           ));
           eventCategories.put("flowers", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Güller we sowgatlar"),
                   new PlaceSubCategoryTranslation("ru", "Цветы и подарки"),
                   new PlaceSubCategoryTranslation("en", "Flowers and gifts")
           ));

           Optional<PlaceCategoryTranslation> eventCategoryTranslation =
                   categoryTranslationService.findByLocaleAndTitle("ru", "Мероприятия");

           eventCategoryTranslation.ifPresent(translation -> {
               PlaceCategory placeCategory = translation.getPlaceCategory();
               eventCategories.forEach((key, value) -> {
                   PlaceSubCategory placeSubCategory = placeSubCategoryService.store(new PlaceSubCategory(placeCategory, value));
                   value.forEach(c -> {
                       c.setPlaceSubCategory(placeSubCategory);
                       subCategoryTranslationService.store(c);
                   });
               });
           });

           Map<String, List<PlaceSubCategoryTranslation>> educationCategories = new HashMap<>();
           educationCategories.put("educational", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Bilim edaralary"),
                   new PlaceSubCategoryTranslation("ru", "Учебные заведения"),
                   new PlaceSubCategoryTranslation("en", "Educational establishments")
           ));
           educationCategories.put("centers", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Bilim merkezleri"),
                   new PlaceSubCategoryTranslation("ru", "Образовательные центры"),
                   new PlaceSubCategoryTranslation("en", "Educational centers")
           ));
           educationCategories.put("libraries", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Kitaphanalar"),
                   new PlaceSubCategoryTranslation("ru", "Библиотеки"),
                   new PlaceSubCategoryTranslation("en", "Libraries")
           ));

           Optional<PlaceCategoryTranslation> eduCategoryTranslation =
                   categoryTranslationService.findByLocaleAndTitle("ru", "Образование");

           eduCategoryTranslation.ifPresent(translation -> {
               PlaceCategory placeCategory = translation.getPlaceCategory();
               educationCategories.forEach((key, value) -> {
                   PlaceSubCategory placeSubCategory = placeSubCategoryService.store(new PlaceSubCategory(placeCategory, value));
                   value.forEach(c -> {
                       c.setPlaceSubCategory(placeSubCategory);
                       subCategoryTranslationService.store(c);
                   });
               });
           });

           Map<String, List<PlaceSubCategoryTranslation>> sportCategories = new HashMap<>();
           sportCategories.put("educational", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Wideo oýunlary"),
                   new PlaceSubCategoryTranslation("ru", "Видео игры"),
                   new PlaceSubCategoryTranslation("en", "Video games")
           ));
           sportCategories.put("cinema", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Kinoteatr"),
                   new PlaceSubCategoryTranslation("ru", "Кинотеатр"),
                   new PlaceSubCategoryTranslation("en", "Cinema")
           ));
           sportCategories.put("theater", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Teatr"),
                   new PlaceSubCategoryTranslation("ru", "Театр"),
                   new PlaceSubCategoryTranslation("en", "Theater")
           ));
           sportCategories.put("museum", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Muzeý"),
                   new PlaceSubCategoryTranslation("ru", "Музей"),
                   new PlaceSubCategoryTranslation("en", "Museum")
           ));
           sportCategories.put("bowling", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Bowling we bilýard"),
                   new PlaceSubCategoryTranslation("ru", "Боулинг и Бильярд"),
                   new PlaceSubCategoryTranslation("en", "Bowling and Billiards")
           ));
           sportCategories.put("playground", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Çagalar meýdançasy"),
                   new PlaceSubCategoryTranslation("ru", "Спортивная площадка"),
                   new PlaceSubCategoryTranslation("en", "Playground")
           ));
           sportCategories.put("fitness", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Fitnes otaglary"),
                   new PlaceSubCategoryTranslation("ru", "Фитнес залы"),
                   new PlaceSubCategoryTranslation("en", "Fitness rooms")
           ));
           sportCategories.put("pool", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Howuz we hammam"),
                   new PlaceSubCategoryTranslation("ru", "Бассейн и Баня"),
                   new PlaceSubCategoryTranslation("en", "Pool and Bath")
           ));
           sportCategories.put("children", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Çagalar üçin"),
                   new PlaceSubCategoryTranslation("ru", "Для детей"),
                   new PlaceSubCategoryTranslation("en", "For children")
           ));

           Optional<PlaceCategoryTranslation> sportTranslation =
                   categoryTranslationService.findByLocaleAndTitle("ru", "Спорт и Отдых");

           sportTranslation.ifPresent(translation -> {
               PlaceCategory placeCategory = translation.getPlaceCategory();
               sportCategories.forEach((key, value) -> {
                   PlaceSubCategory placeSubCategory = placeSubCategoryService.store(new PlaceSubCategory(placeCategory, value));
                   value.forEach(c -> {
                       c.setPlaceSubCategory(placeSubCategory);
                       subCategoryTranslationService.store(c);
                   });
               });
           });

           Map<String, List<PlaceSubCategoryTranslation>> outletsCategories = new HashMap<>();
           outletsCategories.put("phones", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Telefon we esbaplar"),
                   new PlaceSubCategoryTranslation("ru", "Телефоны и Аксессуары"),
                   new PlaceSubCategoryTranslation("en", "Phones and Accessories")
           ));
           outletsCategories.put("supermarket", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Supermarket"),
                   new PlaceSubCategoryTranslation("ru", "Супермаркет"),
                   new PlaceSubCategoryTranslation("en", "Supermarket")
           ));
           outletsCategories.put("appliances", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Enjamlar"),
                   new PlaceSubCategoryTranslation("ru", "Бытовая техника"),
                   new PlaceSubCategoryTranslation("en", "Appliances")
           ));
           outletsCategories.put("plants", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Ösümlikler we haýwan dükanlary"),
                   new PlaceSubCategoryTranslation("ru", "Растения и Зоомагазины"),
                   new PlaceSubCategoryTranslation("en", "Plants and Pet Stores")
           ));
           outletsCategories.put("jewelry", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Şaý-sepler"),
                   new PlaceSubCategoryTranslation("ru", "Ювелирия"),
                   new PlaceSubCategoryTranslation("en", "Jewelry")
           ));
           outletsCategories.put("children", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Çagalar üçin"),
                   new PlaceSubCategoryTranslation("ru", "Для детей"),
                   new PlaceSubCategoryTranslation("en", "For children")
           ));
           outletsCategories.put("home", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Öý üçin"),
                   new PlaceSubCategoryTranslation("ru", "Для дома"),
                   new PlaceSubCategoryTranslation("en", "For home")
           ));
           outletsCategories.put("electronics", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Elektronika"),
                   new PlaceSubCategoryTranslation("ru", "Электроника"),
                   new PlaceSubCategoryTranslation("en", "Electronics")
           ));
           outletsCategories.put("clothing", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Egin-eşik we aýakgap"),
                   new PlaceSubCategoryTranslation("ru", "Одежда и Обувь"),
                   new PlaceSubCategoryTranslation("en", "Clothing and Shoes")
           ));
           outletsCategories.put("office", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Ofis we kansler"),
                   new PlaceSubCategoryTranslation("ru", "Офис и Канцелярия"),
                   new PlaceSubCategoryTranslation("en", "Office and Chancery")
           ));
           outletsCategories.put("perfumes", Arrays.asList(
                   new PlaceSubCategoryTranslation("tm", "Parfýumeriýa we kosmetika"),
                   new PlaceSubCategoryTranslation("ru", "Парфюмерия и Косметика"),
                   new PlaceSubCategoryTranslation("en", "Perfumes and cosmetics")
           ));

           Optional<PlaceCategoryTranslation> outletsTranslation =
                   categoryTranslationService.findByLocaleAndTitle("ru", "Торговые точки");

           outletsTranslation.ifPresent(translation -> {
               PlaceCategory placeCategory = translation.getPlaceCategory();
               outletsCategories.forEach((key, value) -> {
                   PlaceSubCategory placeSubCategory = placeSubCategoryService.store(new PlaceSubCategory(placeCategory, value));
                   value.forEach(c -> {
                       c.setPlaceSubCategory(placeSubCategory);
                       subCategoryTranslationService.store(c);
                   });
               });
           });
       }
    }
}
