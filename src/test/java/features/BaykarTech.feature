
Feature: BaykarTech Navbar Navigasyonu

	Scenario: Ziyaretçi navbar menüsündeki tüm bağlantılara tıklayabilmeli ve sayfalar açılmalı
		Given Kullanıcı 'https://kariyer.baykartech.com/tr' sitesini ziyaret eder
		When Kullanıcı Navigation Bar üzerindeki tüm bağlantılara sırayla tıklar


	Scenario: Kullanıcı dil değiştirerek navbar öğelerinin doğru dilde gösterildiğini görmelidir
		Given Kullanıcı 'https://kariyer.baykartech.com/tr' sitesini ziyaret eder
		When Kullanıcı varsayılan sayfa dilinin Türkçe 'TR' olduğunu doğrular
		Then Navbar üzerindeki öğeler aşağıdaki metinleri içermelidir:
			| KARİYER          |
			| Yüksek İrtifa    |
			| Yerleşkelerimiz  |
			| Sosyal Alanlar   |
			| AÇIK POZİSYONLAR |
			| STAJ             |
			| S.S.S           |
			| BAYKAR           |
			| YÜKSEK İRTİFA    |
			| GİRİŞ            |
			| EN               |
			| TR               |
		When Kullanıcı sayfa dilini İngilizce 'EN' olarak değiştirir
		Then Navbar üzerindeki öğeler aşağıdaki metinleri içermelidir:
			| CAREER        |
			| High Altitude |
			| Our campuses  |
			| Social Areas  |
			| OPEN POSITIONS|
			| INTERNSHIP    |
			| FAQ           |
			| BAYKAR        |
			| YÜKSEK İRTİFA |
			| LOGIN         |
			| TR            |
			| EN            |

  Scenario Outline: Birim filtreleme ve pozisyon başlığı eşleşme kontrolü
    Given Kullanıcı 'https://kariyer.baykartech.com/tr/' sitesini ziyaret eder
	  And Kullanıcı AÇIK POZİSYONLAR butonuna tıklar
	  And Kullanıcı Açık Pozisyonlar butonuna tıklar
    When  Kullanıcı Search Box alanını görene kadar sayfayı aşağa kaydırır
    When  Kullanıcı "<Department>" birimini filtreler
    And   Kullanıcı pozisyonları "<Position>" ile arar
    Then  Listelenen pozisyonların başlığı "<Position>" içermelidir

    Examples:
      | Department                                         | Position                                   |
      | Finans                                        | Bütçe ve Raporlama Uzmanı                       |
      | Web Yazılım Teknolojileri        			  | Web Yazılım Teknolojileri Yazılım Test Uzmanı   |
      | Elektriksel Güç Sistemleri Geliştirme         | Yer Güç Sistemleri Geliştirme Mühendisi         |
      | Kalite Yönetimi                               | Mekanik Giriş Kalite Teknisyeni / Teknikeri     |
      | Silah Sistemleri      			              | Silah Sistemleri Analiz ve Simülasyon Mühendisi |
      | Yapısal Teknolojiler       					  | Termal Analiz Mühendisi (Aviyonik Sistemler)    |