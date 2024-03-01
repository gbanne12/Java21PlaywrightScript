import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Locator.FilterOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

import java.util.ArrayList;
import java.util.List;

void main() {
    try (var playwright = Playwright.create()) {
        var page = playwright
                .chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(false))
                .newContext()
                .newPage();


        // #1 Get all the Hugo winners from Wikipedia
        page.navigate("https://en.wikipedia.org/wiki/Hugo_Award_for_Best_Novel");

        var nomineesTable = page
                .getByRole(AriaRole.TABLE)
                .filter(new FilterOptions().setHas(
                        page.getByText("Winners and nominees", new Page.GetByTextOptions().setExact(true))));

        var winningRows = nomineesTable.locator("[style*='background']").all();

        var bookTitles = new ArrayList<>();
        winningRows.forEach(row -> {
            List<Locator> cellsInRow = row.getByRole(AriaRole.CELL).all();

            cellsInRow.removeIf(
                    cell -> cell.getAttribute("scope") != null &&
                            cell.getAttribute("scope").equals("rowgroup"));

            var column = getColumnPosition("Novel", nomineesTable);
            if (column < cellsInRow.size()) {
                var title = cellsInRow.get(column).innerText();
                bookTitles.add(title);
            }
        });

        //#2  Check LibbyApp to see if any are available
        page.navigate("https://libbyapp.com/library/glasgow");

        page.getByLabel("Open Filters Dialogue").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("books"))
                .filter(new FilterOptions().setHasNotText("audiobooks"))
                .click();

        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("fantasy")).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("science fiction")).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("available now")).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("popularity")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("show")).click();

        var resultTitles = page
                .locator(".title-list")
                .getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("book"))
                .locator(".title-tile-title")
                .allInnerTexts();

        var commonElements = bookTitles.stream()
                .distinct()
                .filter(resultTitles::contains)
                .toList();

        System.out.println(commonElements.isEmpty() ? "No match found" : "Found a match" + commonElements);
    }
}

private int getColumnPosition(String headerText, Locator table) {
    List<Locator> columnHeaders = table.getByRole(AriaRole.COLUMNHEADER).all();

    var listIndex = columnHeaders.indexOf(
            columnHeaders
                    .stream()
                    .filter(header -> header.innerText().contains(headerText))
                    .findFirst()
                    .orElse(null)
    );

    return listIndex - 1;
}

