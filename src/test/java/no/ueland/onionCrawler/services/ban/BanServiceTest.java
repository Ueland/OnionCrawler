package no.ueland.onionCrawler.services.ban;

import static org.hamcrest.MatcherAssert.assertThat;

import com.google.inject.Inject;
import eu.fabiostrozzi.guicejunitrunner.GuiceJUnitRunner;
import no.ueland.onionCrawler.OnionCrawlerModule;
import org.apache.commons.codec.digest.DigestUtils;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({OnionCrawlerModule.class})
public class BanServiceTest {

	private final String md5sum = DigestUtils.md5Hex("testhostname.onion");

	@Inject
	BanService banService;

	@Before
	public void beforeTest() throws Exception {
		banService.addMd5sum(md5sum, "test");
	}

	@After
	public void afterTest() throws Exception {
		banService.removeMd5sum(md5sum);
	}

	@Test
	public void testIsBanned() throws Exception {
		assertThat(banService.isBanned("https://testhostname.onion/test.php"), CoreMatchers.is(true));
		assertThat(banService.isBanned("https://testhostname.onion"), CoreMatchers.is(true));
		assertThat(banService.isBanned("https://testhostname.onion/"), CoreMatchers.is(true));
		assertThat(banService.isBanned("https://testhostname.onion/../"), CoreMatchers.is(true));
		assertThat(banService.isBanned("http://testhostname.onion/../"), CoreMatchers.is(true));
		assertThat(banService.isBanned("https://testhostn2ame.onion/"), CoreMatchers.is(false));
	}
}
