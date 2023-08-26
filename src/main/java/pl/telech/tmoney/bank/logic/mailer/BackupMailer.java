package pl.telech.tmoney.bank.logic.mailer;


import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import pl.telech.tmoney.bank.logic.AccountLogic;
import pl.telech.tmoney.bank.logic.report.ReportService;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.commons.model.shared.FileResult;
import pl.telech.tmoney.commons.utils.TExtensions;

@Slf4j
@Component
@RequiredArgsConstructor
@ExtensionMethod(TExtensions.class)
public class BackupMailer {

	@Value("${java.io.tmpdir}")
	private final String tempDir;
	
	@Value("${tmoney.mailer.backup.db-file}")
	private final String dbFile; 

	@Value("${tmoney.mailer.backup.zip.name}")
	private final String zipName;
	
	@Value("${tmoney.mailer.backup.zip.password}")
	private final String zipPassword;
	
	@Value("${tmoney.mailer.backup.to}")
	private final String mailTo;
	
	@Value("${tmoney.mailer.backup.subject}")
	private final String mailSubject;
	
	@Value("${tmoney.mailer.backup.body}")
	private final String mailBody;
	
	private final AccountLogic accountLogic;
	private final ReportService reportService;
	private final JavaMailSender emailSender;
	
	
	@Scheduled(cron = "${tmoney.mailer.backup.cron}")
	public void run() {
		List<File> files = generateTablePdfs();
		files.add(findDbBackupFile());
		File zipFile = createZipFile(files);
		sendMail(zipFile);
	}
	
	private List<File> generateTablePdfs() {
		return accountLogic.loadAll(true).stream()
			.map(this::generateTableFile)
			.list();
	}
	
	private File generateTableFile(Account account) {
		log.info("Creating table pdf for " + account.getName());
		
		FileResult result = reportService.generateTable(account.getCode());
		File file = new File(tempDir + File.separator + result.getName());
		try {
			FileUtils.writeByteArrayToFile(file, result.getContent());
		} 
		catch (IOException e) {
			throw new TMoneyException("Cannot write report to pdf file", e);
		}
		return file;
	}
	
	private File findDbBackupFile() {
		String filePath = LocalDate.now().format(DateTimeFormatter.ofPattern(dbFile));
		return new File(filePath);
	}
	
	private File createZipFile(List<File> files) {
		ZipParameters zipParameters = new ZipParameters();
		zipParameters.setCompressionLevel(CompressionLevel.ULTRA); 
		zipParameters.setEncryptFiles(true);
		zipParameters.setEncryptionMethod(EncryptionMethod.AES);

		ZipFile zipFile = new ZipFile(tempDir + File.separator + "backup.zip", zipPassword.toCharArray());
		try {
			zipFile.addFiles(files, zipParameters);
			zipFile.close();
		} 
		catch (IOException e) {
			throw new TMoneyException("Cannot create zip file", e);
		}
		return zipFile.getFile();
	}
	
	private void sendMail(File zip) {
		log.info("Sending email");
		
		MimeMessage message = emailSender.createMimeMessage();	     
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
		    helper.setTo(mailTo);
		    helper.setSubject(mailSubject);
		    helper.setText(mailBody);
		        
		    FileSystemResource file = new FileSystemResource(zip);
		    helper.addAttachment(LocalDate.now().format(DateTimeFormatter.ofPattern(zipName)), file);

		    emailSender.send(message);
		    log.info("Email sent");
		} 
		catch (MessagingException e) {
			throw new TMoneyException("Cannot send email", e);
		}   
	}
}
