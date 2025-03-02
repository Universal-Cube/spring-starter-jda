package org.universalcube.spring_starter_discord.discord.pageable;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class PageableMenu {
	protected int maxPage = Integer.MAX_VALUE;
	protected final AtomicInteger currentPage;

	protected PageableMenu() {
		this(1);
	}

	protected PageableMenu(int startPage) {
		this.currentPage = new AtomicInteger(startPage);
	}

	public int getCurrentPage() {
		return this.currentPage.intValue();
	}

	public abstract void render(int page);

	public int previousPage() {
		int initialPage = this.currentPage.intValue();
		if (initialPage <= 1)
			return initialPage;

		this.render(this.currentPage.decrementAndGet());
		return initialPage - 1;
	}

	public int nextPage() {
		int initialPage = this.currentPage.intValue();
		if (initialPage >= this.maxPage)
			return initialPage;

		this.render(this.currentPage.incrementAndGet());
		return initialPage + 1;
	}
}
