package com.fimi.app.x8s.tools;

public class WGS84ToSK42 {
    /// Подстроечные константы, компенсирующие ошибку вычисления координат Х и У в СК-42
    final static double D_X = 8.0;
    final static double D_Y = -11.0;


    // Математические константы
    /// Число угловых секунд в радиане
    private static final double ro = 206264.8062;
    /// Эллипсоид Красовского
    /// Большая полуось
    private static final double aP = 6378245;
    /// Сжатие
    private static final double alP = 1 / 298.3;
    /// Квадрат эксцентриситета
    private static final double e2P = 2 * alP - Math.pow(alP, 2);
    /// Эллипсоид WGS84 (GRS80, эти два эллипсоида сходны по большинству параметров)
    /// Большая полуось
    private static final double aW = 6378137;
    /// Сжатие
    private static final double alW = 1 / 298.257223563;
    /// Квадрат эксцентриситета
    private static final double e2W = 2 * alW - Math.pow(alW, 2);
    private static final double e2 = (e2P + e2W) / 2;
    private static final double de2 = e2W - e2P;
    /// Вспомогательные значения для преобразования эллипсоидов
    private static final double a = (aP + aW) / 2;
    private static final double da = aW - aP;
    /// Линейные элементы трансформирования, в метрах
    private static final double dx = 23.92;
    private static final double dy = -141.27;
    private static final double dz = -80.9;
    /// Угловые элементы трансформирования, в секундах
    private static final double wx = 0;
    private static final double wy = 0;
    private static final double wz = 0;
    /// Дифференциальное различие масштабов
    private static final double ms = 0;


    /// Сближение меридианов

    /**
     * @param lat       широта
     * @param longitude долгота
     * @return сближение меридианов
     */
    public static double gridDeclination(double lat, double longitude) {
        return ((longitude % 6) - 3.0) * Math.sin(lat * Math.PI / 180.0);
    }

    /// Координаты в СК-42

    /**
     * @param latWgs84    широта
     * @param longWgs84   долгота
     * @param heightWgs84 высота
     * @return X Y координаты в СК-42
     */
    public static double[] WGS84ToSK42Meters(double latWgs84, double longWgs84, double heightWgs84) {
        /// Часть 1: Перевод Wgs84 географических координат(долготы и широты в градусах) в СК42 географические координаты (долготу и широту в градусах)//Part 1: Converting Wgs84 geographical coordinates(longitude and latitude in degrees) to SK42 geographical coordinates(longitude and latitude in degrees)
        double ro = 206264.8062;//Число угловых секунд в радиане//The number of angular seconds in radians
        double aP = 6378245; // Большая полуось//Large semi - axis
        double alP = 1 / 298.3; // Сжатие//Compression
        double e2P = 2 * alP - Math.pow(alP, 2); // Квадрат эксцентриситета//Eccentricity square

        /// Эллипсоид WGS84 (GRS80, эти два эллипсоида сходны по большинству параметров)//Ellipsoid WGS84 (GRS80, these two ellipsoids are similar in most parameters)
        double aW = 6378137; // Большая полуось//Large semi - axis
        double alW = 1 / 298.257223563; // Сжатие//Compression
        double e2W = 2 * alW - Math.pow(alW, 2); // Квадрат эксцентриситета//Eccentricity square

        /// Вспомогательные значения для преобразования эллипсоидов
        /// Auxiliary values for converting ellipsoids
        double a1 = (aP + aW) / 2;
        double e21 = (e2P + e2W) / 2;
        double da = aW - aP;
        double de2 = e2W - e2P;

        /// Линейные элементы трансформирования, в метрах//Linear transformation elements, in meters
        double dx = 23.92;
        double dy = -141.27;
        double dz = -80.9;

        /// Угловые элементы трансформирования, в секундах//Angular transformation elements, in seconds
        double wx = 0;
        double wy = 0;
        double wz = 0;

        /// Дифференциальное различие масштабов//Differential difference of scales
        double ms = 0;

        double B, L, M11, N1;
        B = latWgs84 * Math.PI / 180;
        L = longWgs84 * Math.PI / 180;
        M11 = a1 * (1 - e21) / Math.pow((1 - e21 * Math.pow(Math.sin(B), 2)), 1.5);
        N1 = a1 * Math.pow((1 - e21 * Math.pow(Math.sin(B), 2)), -0.5);
        double dB = ro / (M11 + heightWgs84) * (N1 / a1 * e21 * Math.sin(B) * Math.cos(B) * da + (Math.pow(N1, 2) / Math.pow(a1, 2) + 1) * N1 * Math.sin(B) * Math.cos(B) * de2 / 2 - (dx * Math.cos(L) + dy * Math.sin(L)) * Math.sin(B) + dz * Math.cos(B)) - wx * Math.sin(L) * (1 + e21 * Math.cos(2 * B)) + wy * Math.cos(L) * (1 + e21 * Math.cos(2 * B)) - ro * ms * e21 * Math.sin(B) * Math.cos(B);

        //широта в ск42 в градусах//latitude in sk42 in degrees
        double SK42_LatDegrees = latWgs84 - dB / 3600;

        B = latWgs84 * Math.PI / 180;
        L = longWgs84 * Math.PI / 180;
        N1 = a1 * Math.pow((1 - e21 * Math.pow(Math.sin(B), 2)), -0.5);
        double dL = ro / ((N1 + heightWgs84) * Math.cos(B)) * (-dx * Math.sin(L) + dy * Math.cos(L)) + Math.tan(B) * (1 - e21) * (wx * Math.cos(L) + wy * Math.sin(L)) - wz;

        //долгота в ск42 в градусах//longitude in sk42 in degrees
        double SK42_LongDegrees = longWgs84 - dL / 3600;

        /// Часть 2: Перевод СК42 географических координат (широты и долготы в градусах) в СК42 прямоугольные координаты (северное и восточное смещения в метрах)//Part 2: Converting of SK42 geographical coordinates (latitude and longitude in degrees) into SK42 rectangular coordinates(easting and northing in meters)
        /// Номер зоны Гаусса-Крюгера//Number of the Gauss-Kruger zone
        int zone = (int) (SK42_LongDegrees / 6.0 + 1);

        /// Параметры эллипсоида Красовского//Parameters of the Krasovsky ellipsoid
        /// Большая (экваториальная) полуось//Large (equatorial) semi-axis
        double a = 6378245.0;
        /// Малая (полярная) полуось//Small (polar) semi-axis
        double b = 6356863.019;
        /// Эксцентриситет//Eccentricity
        double e2 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2);
        /// Приплюснутость//Flatness
        double n = (a - b) / (a + b);


        /// Параметры зоны Гаусса-Крюгера//Parameters of the Gauss-Kruger zone
        /// Масштабный коэффициент//Scale factor
        double F = 1.0;
        /// Начальная параллель (в радианах)//Initial parallel (in radians)
        double Lat0 = 0.0;
        /// Центральный меридиан (в радианах)//Central Meridian (in radians)
        double Lon0 = (zone * 6 - 3) * Math.PI / 180;
        /// Условное северное смещение для начальной параллели//Conditional north offset for the initial parallel
        double N0 = 0.0;
        /// Условное восточное смещение для центрального меридиана//Conditional eastern offset for the central meridian
        double E0 = zone * 1e6 + 500000.0;

        /// Перевод широты и долготы в радианы//Converting latitude and longitude to radians
        double Lat = SK42_LatDegrees * Math.PI / 180.0;
        double Lon = SK42_LongDegrees * Math.PI / 180.0;

        /// Вычисление переменных для преобразования//Calculating variables for conversion
        double sinLat = Math.sin(Lat);
        double cosLat = Math.cos(Lat);
        double tanLat = Math.tan(Lat);

        double v = a * F * Math.pow(1 - e2 * Math.pow(sinLat, 2), -0.5);
        double p = a * F * (1 - e2) * Math.pow(1 - e2 * Math.pow(sinLat, 2), -1.5);
        double n2 = v / p - 1;
        double M1 = (1 + n + 5.0 / 4.0 * Math.pow(n, 2) + 5.0 / 4.0 * Math.pow(n, 3)) * (Lat - Lat0);
        double M2 = (3 * n + 3 * Math.pow(n, 2) + 21.0 / 8.0 * Math.pow(n, 3)) * Math.sin(Lat - Lat0) * Math.cos(Lat + Lat0);
        double M3 = (15.0 / 8.0 * Math.pow(n, 2) + 15.0 / 8.0 * Math.pow(n, 3)) * Math.sin(2 * (Lat - Lat0)) * Math.cos(2 * (Lat + Lat0));
        double M4 = 35.0 / 24.0 * Math.pow(n, 3) * Math.sin(3 * (Lat - Lat0)) * Math.cos(3 * (Lat + Lat0));
        double M = b * F * (M1 - M2 + M3 - M4);
        double I = M + N0;
        double II = v / 2 * sinLat * cosLat;
        double III = v / 24 * sinLat * Math.pow(cosLat, 3) * (5 - Math.pow(tanLat, 2) + 9 * n2);
        double IIIA = v / 720 * sinLat * Math.pow(cosLat, 5) * (61 - 58 * Math.pow(tanLat, 2) + Math.pow(tanLat, 4));
        double IV = v * cosLat;
        double V = v / 6 * Math.pow(cosLat, 3) * (v / p - Math.pow(tanLat, 2));
        double VI = v / 120 * Math.pow(cosLat, 5) * (5 - 18 * Math.pow(tanLat, 2) + Math.pow(tanLat, 4) + 14 * n2 - 58 * Math.pow(tanLat, 2) * n2);

        /// Вычисление северного и восточного смещения (в метрах)//Calculation of the north and east offset (in meters)
        double N = I + II * Math.pow(Lon - Lon0, 2) + III * Math.pow(Lon - Lon0, 4) + IIIA * Math.pow(Lon - Lon0, 6) + D_X;
        double E = E0 + IV * (Lon - Lon0) + V * Math.pow(Lon - Lon0, 3) + VI * Math.pow(Lon - Lon0, 5) + D_Y;

        return new double[]{N, E};
    }

    /// Вычисление координат цели из текущих координат дрона плюс дистанция и азимут на цель

    /**
     * @param latWgs84    широта
     * @param longWgs84   долгота
     * @param heightWgs84 высота
     * @param a           азимут на цель
     * @param l           дистанция до цели
     * @return сближение меридианов
     */
    public static double[] WGS84ToSK42MetersWithShift(double latWgs84, double longWgs84, double heightWgs84, double a, double l) {
        double gridDecline = gridDeclination(latWgs84, longWgs84);
        double[] sk = WGS84ToSK42Meters(latWgs84, longWgs84, heightWgs84);
        double angleGridProjection = a - gridDecline;
        double x = sk[0] + l * Math.cos(angleGridProjection * Math.PI / 180.0);
        double y = sk[1] + l * Math.sin(angleGridProjection * Math.PI / 180.0);

        return new double[]{x, y};
    }

    /**
     * Пересчет широты из WGS-84 в СК-42.
     *
     * @param Bd широта
     * @param Ld долгота
     * @param H  высота
     * @return широта в Ск-42
     */
    public static double WGS84_SK42_Lat(double Bd, double Ld, double H) {
        return Bd - dB(Bd, Ld, H) / 3600;
    }

    /**
     * Пересчет широты из СК-42 в WGS-84.
     *
     * @param Bd широта
     * @param Ld долгота
     * @param H  высота
     * @return широта в WGS-84
     */
    public static double SK42_WGS84_Lat(double Bd, double Ld, double H) {
        return Bd + dB(Bd, Ld, H) / 3600;
    }

    /**
     * Пересчет долготы из WGS-84 в СК-42.
     *
     * @param Bd широта
     * @param Ld долгота
     * @param H  высота
     * @return долгота в СК-42
     */
    public static double WGS84_SK42_Long(double Bd, double Ld, double H) {
        return Ld - dL(Bd, Ld, H) / 3600;
    }

    /**
     * Пересчет долготы из СК-42 в WGS-84.
     *
     * @param Bd широта
     * @param Ld долгота
     * @param H  высота
     * @return долгота в WGS-84
     */
    public static double SK42_WGS84_Long(double Bd, double Ld, double H) {
        return Ld + dL(Bd, Ld, H) / 3600;
    }

    /**
     * @param Bd широта
     * @param Ld долгота
     * @param H  высота
     * @return
     */
    public static double dB(double Bd, double Ld, double H) {
        double B = Bd * Math.PI / 180;
        double L = Ld * Math.PI / 180;
        double M = a * (1 - e2) / Math.pow((1 - e2 * Math.pow(Math.sin(B), 2)), 1.5);
        double N = a * Math.pow((1 - e2 * Math.pow(Math.sin(B), 2)), -0.5);
        double result = ro / (M + H) * (N / a * e2 * Math.sin(B) * Math.cos(B) * da
                + (Math.pow(N, 2) / Math.pow(a, 2) + 1) * N * Math.sin(B) *
                Math.cos(B) * de2 / 2 - (dx * Math.cos(L) + dy * Math.sin(L)) *
                Math.sin(B) + dz * Math.cos(B)) - wx * Math.sin(L) * (1 + e2 *
                Math.cos(2 * B)) + wy * Math.cos(L) * (1 + e2 * Math.cos(2 * B)) -
                ro * ms * e2 * Math.sin(B) * Math.cos(B);
        return result;
    }

    /**
     * @param Bd широта
     * @param Ld долгота
     * @param H  высота
     * @return
     */
    public static double dL(double Bd, double Ld, double H) {
        double B = Bd * Math.PI / 180;
        double L = Ld * Math.PI / 180;
        double N = a * Math.pow((1 - e2 * Math.pow(Math.sin(B), 2)), -0.5);
        return ro / ((N + H) * Math.cos(B)) * (-dx * Math.sin(L) + dy * Math.cos(L))
                + Math.tan(B) * (1 - e2) * (wx * Math.cos(L) + wy * Math.sin(L)) - wz;
    }

    /**
     * @param Bd широта
     * @param Ld долгота
     * @param H  высота
     * @return
     */
    public static double WGS84Alt(double Bd, double Ld, double H) {
        double B = Bd * Math.PI / 180;
        double L = Ld * Math.PI / 180;
        double N = a * Math.pow((1 - e2 * Math.pow(Math.sin(B), 2)), -0.5);
        double dH = -a / N * da + N * Math.pow(Math.sin(B), 2) * de2 / 2 +
                (dx * Math.cos(L) + dy * Math.sin(L)) *
                        Math.cos(B) + dz * Math.sin(B) - N * e2 *
                Math.sin(B) * Math.cos(B) *
                (wx / ro * Math.sin(L) - wy / ro * Math.cos(L)) +
                (Math.pow(a, 2) / N + H) * ms;
        return H + dH;
    }
}
