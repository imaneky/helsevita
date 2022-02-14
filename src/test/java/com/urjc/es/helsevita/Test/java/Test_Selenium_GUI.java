package com.urjc.es.helsevita.Test.java;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class Test_Selenium_GUI {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\imane\\OneDrive - Universidad Rey Juan Carlos\\Hospitalito\\helseVITA-main\\src\\main\\java\\com\\urjc\\es\\helseVITA\\Test\\java\\Drivers\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://127.0.0.1:8000/");

        driver.findElement(By.xpath("/html/body/div/div[2]/button[3]")).click();
        driver.findElement(By.xpath("/html/body/div/div[3]/p[2]/a")).click();
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/button")).click();
        driver.findElement(By.xpath("/html/body/main/section/form/center/button[1]")).click();
        driver.findElement(By.xpath("/html/body/main/section/div/form/div[1]/input")).sendKeys("ClaudiaParra");
        driver.findElement(By.xpath("/html/body/main/section/div/form/div[2]/input[1]")).sendKeys("1234Claudia");
        driver.findElement(By.xpath("/html/body/main/section/div/form/button")).click();
        driver.findElement(By.xpath("/html/body/main/section/div/form[1]/button[1]")).click();

        driver.findElement(By.xpath("/html/body/main/section/div/form/center/input[1]")).sendKeys("13122023");
        driver.findElement(By.xpath("/html/body/main/section/div/form/center/input[1]")).sendKeys(Keys.ARROW_RIGHT);
        driver.findElement(By.xpath("/html/body/main/section/div/form/center/input[1]")).sendKeys("1212");

        driver.findElement(By.xpath("/html/body/main/section/div/form/center/button")).click();
        driver.findElement(By.xpath("/html/body/main/section/div/div/center/button")).click();
        driver.findElement(By.xpath("/html/body/main/section/div/form[1]/button[2]")).click();

        //Let's verify if the appointment was correctly saved

        String text = driver.findElement(By.xpath("/html/body/main/section/div/div[2]/div/div/div[3]")).getText();

        Assert.assertTrue(text.contains("1"));



    }
}