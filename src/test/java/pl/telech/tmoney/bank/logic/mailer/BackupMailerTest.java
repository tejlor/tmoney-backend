package pl.telech.tmoney.bank.logic.mailer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.List;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Session;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.bank.builder.AccountBuilder;
import pl.telech.tmoney.bank.logic.AccountLogic;
import pl.telech.tmoney.bank.logic.report.ReportService;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.model.shared.FileResult;


@ExtendWith(SpringExtension.class)
@Import(BackupMailer.class)
@TestPropertySource(properties = {
		"java.io.tmpdir=/tmp",
		"tmoney.mailer.backup.db-file='/tmp/tmoney-backup-'yyyy-MM-dd'.sql'",
		"tmoney.mailer.backup.zip.name='backup-'yyyy-MM-dd'.sql'"
})
class BackupMailerTest {

	@MockBean
	AccountLogic accountLogic;
	
	@MockBean
	ReportService reportService;
	
	@MockBean
	JavaMailSender mailSender;
	
	@Autowired
	BackupMailer backupMailer;
	
	@Test
	void run() throws MessagingException, IOException {
		// given
		Account account = new AccountBuilder().build();
		FileResult pdfTable = new FileResult("Bank.pdf", "pdf", new byte[10]);
		FileUtils.write(new File("/tmp/tmoney-backup-" + LocalDate.now() + ".sql"), "sql", Charset.defaultCharset());
		
		when(accountLogic.loadAll(true)).thenReturn(List.of(account));
		when(reportService.generateTable(account.getCode())).thenReturn(pdfTable);
		when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session)null));
		
		// when
		backupMailer.run();
		
		// then
		verify(reportService).generateTable(account.getCode());
		var messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
		verify(mailSender).send(messageCaptor.capture());
		
		MimeMessage message = messageCaptor.getValue();
		assertThat(message.getRecipients(RecipientType.TO)[0].toString()).isEqualTo("${tmoney.mailer.backup.to}");
		assertThat(message.getSubject()).isEqualTo("${tmoney.mailer.backup.subject}");		
		assertThat(((MimeMultipart) message.getContent()).getCount()).isEqualTo(2);
	}
}
