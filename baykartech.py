import logging
from locust import HttpUser, task, between, SequentialTaskSet
from bs4 import BeautifulSoup
import random

logger = logging.getLogger("locust.user")  # Locust'un log kategorisi

class BaykarKariyerUserBehavior(SequentialTaskSet):

    def on_start(self):
        logger.info("Test Başlatılıyor...")
        self.client.headers.update({
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
        })

    @task
    def main_page(self):
        try:
            with self.client.get("", name="Ana Sayfa", catch_response=True) as response:
                if response.status_code == 200:
                    response.success()
                    logger.info("AnaSayfa başarıyla yüklendi.")
                else:
                    response.failure(f"AnaSayfa yüklenemedi: {response.status_code}")
        except Exception as e:
            logger.error(f"AnaSayfa isteğinde hata: {e}")

    @task
    def login_page(self):
        try:
            with self.client.get("/hesaplar/login/?next=/tr/dashboard/", name="Login Sayfası", catch_response=True) as response:
                if response.status_code == 200:
                    soup = BeautifulSoup(response.text, 'html.parser')
                    login_title = soup.find("p", class_="resetText")
                    if login_title and "Giriş" in login_title.text:
                        response.success()
                        logger.info("Login sayfası başarıyla yüklendi.")
                    else:
                        response.failure("Login sayfasında 'Giriş' yazısı bulunamadı.")
                else:
                    response.failure(f"Login sayfası yüklenemedi: {response.status_code}")
        except Exception as e:
            logger.error(f"Login sayfası isteğinde hata: {e}")

    @task
    def login(self):
        try:
            payload = {
                "username": "testuser", ##
                "password": "testpass"
            }
            with self.client.post("/hesaplar/login/",name="Login Baykartech", data=payload, allow_redirects=True, catch_response=True) as response:
                if response.status_code == 200 and "/tr/dashboard" in response.url:
                    response.success()
                    logger.info("Login işlemi başarılı.")
                else:
                    response.failure(f"Login işlemi başarısız: {response.status_code}")
        except Exception as e:
            logger.error(f"Login isteğinde hata: {e}")

    @task
    def open_positions(self):
        try:
            with self.client.get("/open-positions/?type=1", name="Open Positions Page", catch_response=True) as response:
                if response.status_code == 200:
                    soup = BeautifulSoup(response.text, 'html.parser')
                    title = soup.find("title")
                    if title and "Baykar Kariyer |  Açık Pozisyonlar" in title.text:
                        response.success()
                        logger.info("Open Positions sayfası başarıyla yüklendi.")
                    else:
                        response.failure("Sayfa title'ında beklenen metin bulunamadı.")
                else:
                    response.failure(f"Sayfa yüklenemedi: {response.status_code}")
        except Exception as e:
            logger.error(f"Open Positions isteğinde hata: {e}")

    @task
    def filtered_position(self):
        category_ids = ["34", "12", "7"]

        selected_category = random.choice(category_ids)
        params = {
            "page": "1",
            "search": "",
            "program_category_ids": selected_category,
            "type": "1"
        }
        try:
            with self.client.get(
                "/application/serializerProgram/",
                params=params,
                headers={"accept": "*/*"},
                name=f"Filtered Position {selected_category}",
                catch_response=True
            ) as response:
                if response.status_code == 200:
                    response.success()
                    logger.info(f"Filtrelenen pozisyon başarılı şekilde yüklendi - Kategori: {selected_category}")
                else:
                    response.failure(f"Status code: {response.status_code}")
        except Exception as e:
            logger.error(f"Serializer programı isteğinde hata: {e}")


class WebsiteUser(HttpUser):
    tasks = [BaykarKariyerUserBehavior]
    wait_time = between(1, 5)
    host = "https://kariyer.baykartech.com/tr"
