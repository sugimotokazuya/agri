package agri.controller;

import org.slim3.controller.router.RouterImpl;

public class AppRouter extends RouterImpl {

    public AppRouter() {
        addRouting(
            "/torihikisaki/edit/{key}/{version}",
            "/torihikisaki/edit?key={key}&version={version}");
        addRouting(
            "/torihikisaki/delete/{key}/{version}",
            "/torihikisaki/delete?key={key}&version={version}");
        addRouting(
            "/torihikisaki/okurisaki/index/{key}/{version}",
            "/torihikisaki/okurisaki/index?key={key}&version={version}");
        addRouting(
            "/torihikisaki/okurisaki/edit/{key}/{version}",
            "/torihikisaki/okurisaki/edit?key={key}&version={version}");
        addRouting(
            "/torihikisaki/okurisaki/delete/{key}/{version}",
            "/torihikisaki/okurisaki/delete?key={key}&version={version}");
        addRouting(
            "/hinmoku/edit/{key}/{version}",
            "/hinmoku/edit?key={key}&version={version}");
        addRouting(
            "/hinmoku/delete/{key}/{version}",
            "/hinmoku/delete?key={key}&version={version}");
        addRouting(
            "/hanbai/price/edit/{key}/{version}",
            "/hanbai/price/edit?key={key}&version={version}");
        addRouting(
            "/hanbai/price/delete/{key}/{version}",
            "/hanbai/price/delete?key={key}&version={version}");
        addRouting(
            "/shinkoku/kamoku/edit/{key}/{version}",
            "/shinkoku/kamoku/edit?key={key}&version={version}");
        addRouting(
            "/shinkoku/kamoku/delete/{key}/{version}",
            "/shinkoku/kamoku/delete?key={key}&version={version}");
        addRouting(
            "/shinkoku/tekiyo/edit/{key}/{version}",
            "/shinkoku/tekiyo/edit?key={key}&version={version}");
        addRouting(
            "/shinkoku/tekiyo/delete/{key}/{version}",
            "/shinkoku/tekiyo/delete?key={key}&version={version}");
        addRouting(
            "/shinkoku/shiwake/delete/{key}/{version}",
            "/shinkoku/shiwake/delete?key={key}&version={version}");
        addRouting(
            "/hatake/edit/{key}/{version}",
            "/hatake/edit?key={key}&version={version}");
        addRouting(
            "/hatake/delete/{key}/{version}",
            "/hatake/delete?key={key}&version={version}");
        addRouting(
            "/sehi/hiryo/edit/{key}/{version}",
            "/sehi/hiryo/edit?key={key}&version={version}");
        addRouting(
            "/sehi/hiryo/delete/{key}/{version}",
            "/sehi/hiryo/delete?key={key}&version={version}");
        addRouting(
            "/sehi/sokutei/edit/{key}/{version}",
            "/sehi/sokutei/edit?key={key}&version={version}");
        addRouting(
            "/sehi/sokutei/delete/{key}/{version}",
            "/sehi/sokutei/delete?key={key}&version={version}");
        addRouting(
            "/sehi/sekkei/{key}/{version}",
            "/sehi/sekkei/index?key={key}&version={version}");
        addRouting(
            "/hanbai/shukka/edit/{key}/{version}",
            "/hanbai/shukka/edit?key={key}&version={version}");
        addRouting(
            "/hanbai/shukka/delete/{key}/{version}",
            "/hanbai/shukka/delete?key={key}&version={version}");
        addRouting(
            "/hanbai/shukka/okurijo/create/{year}/{month}/{day}",
            "/hanbai/shukka/okurijo/create?year={year}&month={month}&day={day}");
        addRouting(
            "/hanbai/shukka/copy/{key}/{version}",
            "/hanbai/shukka/copy?key={key}&version={version}");
        addRouting(
            "/chokubai/edit/{key}/{version}",
            "/chokubai/edit?key={key}&version={version}");
        addRouting(
            "/chokubai/delete/{key}/{version}",
            "/chokubai/delete?key={key}&version={version}");
        addRouting(
            "/hanbai/shukka/chokubaiEdit/{key}",
            "/hanbai/shukka/chokubaiEdit?key={key}");
        addRouting(
            "/hanbai/pdfNohin/{key}/{version}",
            "/hanbai/pdfNohin?key={key}&version={version}");
        addRouting(
            "/hanbai/pdfNohins/{year}/{month}/{day}",
            "/hanbai/pdfNohins?year={year}&month={month}&day={day}");
        addRouting(
            "/hanbai/printNohin/{key}/{version}",
            "/hanbai/printNohin?key={key}&version={version}");
        addRouting(
            "/hanbai/printNohin2/{key}/{version}",
            "/hanbai/printNohin2?key={key}&version={version}");
        addRouting(
            "/hanbai/printNohin3/{key}/{version}",
            "/hanbai/printNohin3?key={key}&version={version}");
        addRouting(
            "/hanbai/seikyu/edit/{key}/{version}",
            "/hanbai/seikyu/edit?key={key}&version={version}");
        addRouting(
            "/hanbai/seikyu/delete/{key}/{version}",
            "/hanbai/seikyu/delete?key={key}&version={version}");
        addRouting(
            "/hanbai/seikyu/kaishuOn/{key}/{version}",
            "/hanbai/seikyu/kaishuOn?key={key}&version={version}");
        addRouting(
            "/hanbai/seikyu/kaishuOff/{key}/{version}",
            "/hanbai/seikyu/kaishuOff?key={key}&version={version}");
        addRouting(
            "/hanbai/seikyu/print/{key}/{version}",
            "/hanbai/seikyu/print?key={key}&version={version}");
        addRouting(
            "/_ah/mail/{address}",
            "/receive?address={address}");
        addRouting(
            "/mail/sokuho/{key}/{version}",
            "/mail/sokuho?key={key}&version={version}");
        addRouting(
            "/mail/one/{key}/{version}",
            "/mail/one?key={key}&version={version}");
        addRouting(
            "/mail/chokubai/{year}/{month}/{day}",
            "/mail/chokubai?year={year}&month={month}&day={day}");
        /*
        addRouting(
            "/mail/sokuho/{year}/{month}/{day}/{hour}",
            "/mail/sokuho?year={year}&month={month}&day={day}&hour={hour}");
        */
        addRouting(
            "/mail/sokuhoIFrame/{key}/{version}",
            "/mail/sokuho?key={key}&version={version}&chokubaiEdit=true");
        addRouting(
            "/mail/notice/{year}/{month}",
            "/mail/notice?year={year}&month={month}");
        addRouting(
            "/mail/delete/{key}/{version}",
            "/mail/delete?key={key}&version={version}");
        addRouting(
            "/mail/onDelete/{key}/{version}",
            "/mail/onDelete?key={key}&version={version}");
        addRouting(
            "/hanbai/chokubai/edit/{key}/{version}",
            "/hanbai/chokubai/edit?key={key}&version={version}");
        addRouting(
            "/user/edit/{key}/{version}",
            "/user/edit?key={key}&version={version}");
        addRouting(
            "/user/delete/{key}/{version}",
            "/user/delete?key={key}&version={version}");
        addRouting(
            "/sagyo/item/edit/{key}/{version}",
            "/sagyo/item/edit?key={key}&version={version}");
        addRouting(
            "/sagyo/item/delete/{key}/{version}",
            "/sagyo/item/delete?key={key}&version={version}");
        addRouting(
            "/sagyo/sakuduke/edit/{key}/{version}",
            "/sagyo/sakuduke/edit?key={key}&version={version}");
        addRouting(
            "/sagyo/sakuduke/kiroku/{key}",
            "/sagyo/sakuduke/kiroku?key={key}");
        addRouting(
            "/sagyo/sakuduke/delete/{key}/{version}",
            "/sagyo/sakuduke/delete?key={key}&version={version}");
        addRouting(
            "/sagyo/shizai/edit/{key}/{version}",
            "/sagyo/shizai/edit?key={key}&version={version}");
        addRouting(
            "/sagyo/shizai/delete/{key}/{version}",
            "/sagyo/shizai/delete?key={key}&version={version}");
        addRouting(
            "/sagyo/kikai/edit/{key}/{version}",
            "/sagyo/kikai/edit?key={key}&version={version}");
        addRouting(
            "/sagyo/kikai/delete/{key}/{version}",
            "/sagyo/kikai/delete?key={key}&version={version}");
        addRouting(
            "/sagyo/edit/{key}/{version}/{page}",
            "/sagyo/edit?key={key}&version={version}&page={page}");
        addRouting(
            "/sagyo/delete/{key}/{version}/{page}",
            "/sagyo/delete?key={key}&version={version}&page={page}");
        addRouting(
            "/sagyo/view/{key}",
            "/sagyo/view?key={key}");
        addRouting(
            "/yago/edit/{key}/{version}",
            "/yago/edit?key={key}&version={version}");
        addRouting(
            "/yago/delete/{key}/{version}",
            "/yago/delete?key={key}&version={version}");
        addRouting(
            "/oshirase/edit/{key}/{version}",
            "/oshirase/edit?key={key}&version={version}");
        addRouting(
            "/oshirase/delete/{key}/{version}",
            "/oshirase/delete?key={key}&version={version}");
        addRouting(
            "/gazo/view/{key}",
            "/gazo/view?key={key}");
        addRouting(
            "/gazo/viewThumb/{key}",
            "/gazo/viewThumb?key={key}");
        addRouting(
            "/gazo/delete/{key}",
            "/gazo/delete?key={key}");
        addRouting(
            "/gazo/useGazo/{gazoKey}/{sagyoKey}",
            "/gazo/useGazo?gazoKey={gazoKey}&sagyoKey={sagyoKey}");
        addRouting(
            "/gazo/kaijo/{key}",
            "/gazo/kaijo?key={key}");
        addRouting(
            "/yago/user/{key}/{version}",
            "/yago/user?key={key}&version={version}");
        addRouting(
            "/kintai/dakoku/{key}/{status}",
            "/kintai/dakoku?key={key}&status={status}");
        addRouting(
            "/kintai/dakokuTeisei/{key}/{status}",
            "/kintai/dakokuTeisei?key={key}&status={status}");
        addRouting(
            "/kintai/teiji/edit/{key}/{version}",
            "/kintai/teiji/edit?key={key}&version={version}");
        addRouting(
            "/kintai/teiji/delete/{key}/{version}",
            "/kintai/teiji/delete?key={key}&version={version}");
        addRouting(
            "/kintai/shinsei/{status}/{userKey}/{year}/{month}/{day}",
            "/kintai/shinsei?status={status}&userKey={userKey}&year={year}&month={month}&day={day}");
        addRouting(
            "/kintai/shoninUpdate/{shonin}/{key}/{version}",
            "/kintai/shoninUpdate?shonin={shonin}&key={key}&version={version}");
        addRouting(
            "/kintai/shinseiDelete/{key}/{version}",
            "/kintai/shinseiDelete?key={key}&version={version}");
        addRouting(
            "/kintai/zangyoDelete/{key}/{version}",
            "/kintai/zangyoDelete?key={key}&version={version}");
        addRouting(
            "/kintai/teishutsu/{userKey}",
            "/kintai/teishutsu?userKey={userKey}");
        addRouting(
            "/kintai/kinmuhyo/view/{key}/{version}",
            "/kintai/kinmuhyo/view?key={key}&version={version}");
        addRouting(
            "/kintai/kinmuhyo/print/{key}/{version}",
            "/kintai/kinmuhyo/print?key={key}&version={version}");
        addRouting(
            "/kintai/kinmuhyo/printA/{key}/{version}",
            "/kintai/kinmuhyo/printA?key={key}&version={version}");
        addRouting(
            "/kintai/kinmuhyo/shonin/{key}/{version}",
            "/kintai/kinmuhyo/shonin?key={key}&version={version}");
        addRouting(
            "/kintai/kinmuhyo/torikeshi/{key}/{version}",
            "/kintai/kinmuhyo/torikeshi?key={key}&version={version}");
        addRouting(
            "/kintai/shift/update/{key}/{version}/{yasumi}",
            "/kintai/shift/update?key={key}&version={version}&yasumi={yasumi}");
        addRouting(
            "/hanbai/keitai/edit/{key}/{version}",
            "/hanbai/keitai/edit?key={key}&version={version}");
        addRouting(
            "/hanbai/keitai/delete/{key}/{version}",
            "/hanbai/keitai/delete?key={key}&version={version}");
        addRouting(
            "/hanbai/keitai/togo/{key}/{version}",
            "/hanbai/keitai/togo?key={key}&version={version}");
        addRouting(
            "/hanbai/kakuduke/delete/{key}/{version}",
            "/hanbai/kakuduke/delete?key={key}&version={version}");
    }

}