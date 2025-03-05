package org.universalcube.spring_starter_discord.discord.pageable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * An abstract class designed to manage a pageable menu system.
 * This class provides functionality for navigating between pages
 * and rendering pages based on the current page state. It is
 * intended to be extended to create custom pageable menu
 * implementations.
 */
public abstract class PageableMenu {
	protected int maxPage = Integer.MAX_VALUE;
	protected final AtomicInteger currentPage;

	/**
	 * Constructs a new PageableMenu with the default starting page.
	 * The default starting page is set to 1.
	 * <p>
	 * This constructor initializes the paging system with the first page
	 * as the current active page. It serves as a base setup for managing
	 * a pageable menu system in an extendable manner.
	 */
	protected PageableMenu() {
		this(1);
	}

	/**
	 * Constructs a new {@code PageableMenu} with the specified starting page.
	 * Initializes the paging system, setting the provided page number as the current page.
	 *
	 * @param startPage the page number to set as the starting page. Must be a positive integer.
	 */
	protected PageableMenu(int startPage) {
		this.currentPage = new AtomicInteger(startPage);
	}

	/**
	 * Retrieves the current page number being displayed or managed by the pageable menu.
	 *
	 * @return The current page number as an integer.
	 */
	public int getCurrentPage() {
		return this.currentPage.intValue();
	}

	/**
	 * Renders the contents of a specific page in the pageable menu system.
	 * This method is abstract and should be implemented by subclasses to define
	 * how the page content is displayed or processed.
	 *
	 * @param page the page number to render. Must be a positive integer
	 *             within the allowable page range.
	 */
	public abstract void render(int page);

	/**
	 * Navigates to the previous page in the pageable menu system if the current page
	 * is not the first page. Updates the current page state and renders the corresponding page.
	 *
	 * @return The number of the previous page if the current page was greater than 1,
	 * or the current page number if it was already the first page.
	 */
	public int previousPage() {
		int initialPage = this.currentPage.intValue();
		if (initialPage <= 1)
			return initialPage;

		this.render(this.currentPage.decrementAndGet());
		return initialPage - 1;
	}

	/**
	 * Advances the pageable menu to the next page if the current page
	 * is not already the last page. The method updates the current page
	 * state and renders the corresponding page.
	 *
	 * @return The number of the new current page after increment or
	 * the current page if it was at the maximum.
	 */
	public int nextPage() {
		int initialPage = this.currentPage.intValue();
		if (initialPage >= this.maxPage)
			return initialPage;

		this.render(this.currentPage.incrementAndGet());
		return initialPage + 1;
	}
}
